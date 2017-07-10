/**
 * Copyright 2014-2017 Carmen Alvarez
 *
 * This file is part of Pálida Muerte.
 *
 * Pálida Muerte is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Pálida Muerte is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pálida Muerte. If not, see <http://www.gnu.org/licenses/>.
 */
package ca.rmen.android.palidamuerte.app.poem.detail;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.rmen.android.palidamuerte.Constants;
import ca.rmen.android.palidamuerte.MusicPlayer;
import ca.rmen.android.palidamuerte.R;
import ca.rmen.android.palidamuerte.app.poem.list.PoemListActivity;
import ca.rmen.android.palidamuerte.provider.poem.PoemContentValues;
import ca.rmen.android.palidamuerte.provider.poem.PoemCursor;
import ca.rmen.android.palidamuerte.provider.poem.PoemSelection;
import ca.rmen.android.palidamuerte.ui.ActionBar;
import ca.rmen.android.palidamuerte.ui.Font;

/**
 * A fragment representing a single poem detail screen.
 * This fragment is either contained in a {@link PoemListActivity} in two-pane mode (on tablets) or a {@link PoemDetailActivity} on handsets.
 */
public class PoemDetailFragment extends Fragment { // NO_UCD (use default)
    private static final String TAG = Constants.TAG + PoemDetailFragment.class.getSimpleName();
    private Handler mHandler;
    private boolean mIsFavorite;
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PoemDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mHandler = new Handler();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        final View rootView = inflater.inflate(R.layout.fragment_poem_detail, container, false);
        TextView tvTitleView = (TextView) rootView.findViewById(R.id.title);
        Typeface font = Font.getTypeface(getActivity());
        tvTitleView.setTypeface(font);
        updateView(getActivity(), rootView);
        return rootView;
    }

    private void updateView(final Activity activity, final View rootView) {
        Log.v(TAG, "updateView");
        new AsyncTask<Void, Void, PoemCursor>() {

            @Override
            protected PoemCursor doInBackground(Void... params) {
                if (getArguments().containsKey(ARG_ITEM_ID)) {
                    long poemId = getArguments().getLong(ARG_ITEM_ID);
                    PoemCursor poemCursor = new PoemSelection().id(poemId).query(activity.getContentResolver());
                    if (poemCursor.moveToFirst()) return poemCursor;
                    poemCursor.close();
                }
                return null;
            }

            @Override
            protected void onPostExecute(PoemCursor poemCursor) {
                boolean favorite = poemCursor.getIsFavorite();
                if (favorite != mIsFavorite) {
                    mIsFavorite = favorite;
                    activity.invalidateOptionsMenu();
                }
                TextView tvTitleView = (TextView) rootView.findViewById(R.id.title);
                tvTitleView.setText(poemCursor.getTitle());
                String preContent = poemCursor.getPreContent();
                TextView preContentView = (TextView) rootView.findViewById(R.id.pre_content);
                preContentView.setVisibility(TextUtils.isEmpty(preContent) ? View.GONE : View.VISIBLE);
                preContentView.setText(preContent);
                ((TextView) rootView.findViewById(R.id.content)).setText(poemCursor.getContent());

                String poemTypeAndNumber = Poems.getPoemNumberString(activity, poemCursor);
                TextView tvPoemTypeAndNumber = (TextView) rootView.findViewById(R.id.poem_type_and_number);
                tvPoemTypeAndNumber.setVisibility(TextUtils.isEmpty(poemTypeAndNumber) ? View.GONE : View.VISIBLE);
                tvPoemTypeAndNumber.setText(poemTypeAndNumber);

                String locationDateString = Poems.getLocationDateString(activity, poemCursor);
                ((TextView) rootView.findViewById(R.id.author)).setText(R.string.author);
                ((TextView) rootView.findViewById(R.id.location_and_date)).setText(locationDateString);

                poemCursor.close();
            }
        }.execute();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.v(TAG, "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        ActionBar.updateMusicMenuItem(getActivity(), menu.findItem(R.id.action_music));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.v(TAG, "onPrepareOptionsMenu");
        super.onPrepareOptionsMenu(menu);
        MenuItem fav = menu.findItem(R.id.action_favorite);
        if (fav == null) {
            Log.v(TAG, "Menu not inflated yet?");
            return;
        }
        if (mIsFavorite) {
            fav.setTitle(R.string.action_favorite_activated);
            fav.setIcon(R.drawable.ic_action_favorite_activated);
        } else {
            fav.setTitle(R.string.action_favorite_normal);
            fav.setIcon(R.drawable.ic_action_favorite_normal);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            MenuItem print = menu.findItem(R.id.action_print);
            print.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "onOptionsItemSelected");
        final long poemId = getArguments().getLong(ARG_ITEM_ID);
        if (item.getItemId() == R.id.action_favorite) {
            final Activity activity = getActivity();
            final View rootView = getView();
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    // Write the poem favorite field.
                    PoemSelection poemSelection = new PoemSelection().id(poemId);
                    PoemCursor cursor = poemSelection.query(activity.getContentResolver());
                    try {
                        if (cursor.moveToFirst()) {
                            boolean wasFavorite = cursor.getIsFavorite();
                            boolean isFavorite = !wasFavorite;
                            new PoemContentValues().putIsFavorite(isFavorite).update(activity.getContentResolver(), poemSelection);
                        }
                    } finally {
                        cursor.close();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void params) {
                    // Reread the poem
                    updateView(activity, rootView);
                }

            }.execute();
        } else if (item.getItemId() == R.id.action_print && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            PoemPrinter.print(getContext(), poemId);
        } else if (item.getItemId() == R.id.action_music) {
            MusicPlayer.getInstance(getActivity()).toggle();
            final Activity activity = getActivity();
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    activity.invalidateOptionsMenu();
                }
            }, 200);
        }
        return super.onOptionsItemSelected(item);
    }
}
