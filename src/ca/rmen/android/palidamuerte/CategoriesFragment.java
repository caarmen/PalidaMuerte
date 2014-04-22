/**
 * Copyright 2014 Carmen Alvarez
 *
 * This file is part of P‡lida Muerte.
 *
 * P‡lida Muerte is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * P‡lida Muerte is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with P‡lida Muerte. If not, see <http://www.gnu.org/licenses/>.
 */
package ca.rmen.android.palidamuerte;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import ca.rmen.android.palidamuerte.app.CategoriesCursorAdapter;
import ca.rmen.android.palidamuerte.provider.category.CategoryColumns;
import ca.rmen.android.palidamuerte.provider.category.CategoryCursor;

public class CategoriesFragment extends Fragment {

    private static final String TAG = Constants.TAG + CategoriesFragment.class.getSimpleName();
    private static final int URL_LOADER = 0;
    private View mProgressBar;

    public CategoriesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, null);
        mProgressBar = view.findViewById(R.id.progress_container);
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setOnItemClickListener(mOnItemClickListener);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.v(TAG, "onAttach");
        super.onAttach(activity);

        getLoaderManager().restartLoader(URL_LOADER, null, mLoaderCallbacks);
    }

    private LoaderCallbacks<Cursor> mLoaderCallbacks = new LoaderCallbacks<Cursor>() {
        private CategoriesCursorAdapter mAdapter = null;

        @Override
        public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
            Log.v(TAG, "onCreateLoader, loaderId = " + loaderId + ", bundle = " + bundle);
            // TODO hardcoded category and series
            CursorLoader loader = new CursorLoader(getActivity(), CategoryColumns.CONTENT_URI, null, null, null, null);
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            Log.v(TAG, "onLoadFinished, loader = " + loader + ", cursor = " + cursor);
            if (mAdapter == null) {
                Activity activity = getActivity();
                if (activity != null) {
                    mAdapter = new CategoriesCursorAdapter(activity);
                    GridView gridView = (GridView) getActivity().findViewById(R.id.gridview);
                    gridView.setAdapter(mAdapter);

                }
            }
            mAdapter.changeCursor(new CategoryCursor(cursor));
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            Log.v(TAG, "onLoaderReset " + loader);
            if (mAdapter != null) mAdapter.changeCursor(null);
            mProgressBar.setVisibility(View.GONE);
        }
    };

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), PoemListActivity.class);
            intent.putExtra(PoemListActivity.EXTRA_CATEGORY_ID, id);
            startActivity(intent);
        }
    };

}
