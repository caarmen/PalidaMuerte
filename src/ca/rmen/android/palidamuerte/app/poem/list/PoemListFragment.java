/**
 * Copyright 2014 Carmen Alvarez
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
package ca.rmen.android.palidamuerte.app.poem.list;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ca.rmen.android.palidamuerte.Constants;
import ca.rmen.android.palidamuerte.R;
import ca.rmen.android.palidamuerte.app.category.Categories;
import ca.rmen.android.palidamuerte.app.poem.detail.PoemDetailFragment;
import ca.rmen.android.palidamuerte.app.poem.detail.Poems;
import ca.rmen.android.palidamuerte.provider.poem.PoemColumns;
import ca.rmen.android.palidamuerte.provider.poem.PoemCursor;
import ca.rmen.android.palidamuerte.provider.poem.PoemSelection;
import ca.rmen.android.palidamuerte.ui.Font;

/**
 * A list fragment representing a list of poems. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link PoemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks} interface.
 */
public class PoemListFragment extends ListFragment { // NO_UCD (use default)

    private static final String TAG = Constants.TAG + PoemListFragment.class.getSimpleName();
    private static final int URL_LOADER = 0;
    private CursorAdapter mAdapter;
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(long id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(long id) {
            Log.v(TAG, "onItemSelected: id = " + id);
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PoemListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity activity = getActivity();
        final long categoryId = activity.getIntent().getLongExtra(PoemListActivity.EXTRA_CATEGORY_ID, -1);
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return Categories.getCategoryName(activity, categoryId);
            }

            @Override
            protected void onPostExecute(String categoryName) {
                activity.getActionBar().setTitle(categoryName);
            }

        }.execute();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = getListView();
        ViewGroup parent = (ViewGroup) listView.getParent();
        View emptyListView = getActivity().getLayoutInflater().inflate(R.layout.empty_list, parent, false);
        TextView emptyTitle = (TextView) emptyListView.findViewById(R.id.title);
        emptyTitle.setTypeface(Font.getTypeface(getActivity()));
        parent.addView(emptyListView);
        listView.setEmptyView(emptyListView);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        getLoaderManager().restartLoader(URL_LOADER, null, mLoaderCallbacks);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Log.v(TAG, "onListItemClick: position = " + position + ", id = " + id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    private LoaderCallbacks<Cursor> mLoaderCallbacks = new LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
            Log.v(TAG, "onCreateLoader, loaderId = " + loaderId + ", bundle = " + bundle);
            Activity activity = getActivity();
            Intent intent = activity.getIntent();
            PoemSelection poemSelection = Poems.getPoemSelection(activity, intent);
            CursorLoader loader = new CursorLoader(activity, PoemColumns.CONTENT_URI, null, poemSelection.sel(), poemSelection.args(), PoemColumns.CATEGORY_ID
                    + ", " + PoemColumns._ID);
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            Log.v(TAG, "onLoadFinished, loader = " + loader + ", cursor = " + cursor);
            if (mAdapter == null) {
                long categoryId = getActivity().getIntent().getLongExtra(PoemListActivity.EXTRA_CATEGORY_ID, -1);
                if (categoryId == Categories.FAVORITE_CATEGORY_ID) mAdapter = new FavoritePoemListCursorAdapter(getActivity());
                else

                    mAdapter = new PoemListCursorAdapter(getActivity());
                setListAdapter(mAdapter);
            }
            mAdapter.changeCursor(new PoemCursor(cursor));
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            Log.v(TAG, "onLoaderReset " + loader);
            if (mAdapter != null) mAdapter.changeCursor(null);
        }
    };
}
