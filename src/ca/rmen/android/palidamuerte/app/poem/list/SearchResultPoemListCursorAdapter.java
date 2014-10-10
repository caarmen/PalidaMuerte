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

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ca.rmen.android.palidamuerte.Constants;
import ca.rmen.android.palidamuerte.R;
import ca.rmen.android.palidamuerte.app.category.Categories;
import ca.rmen.android.palidamuerte.app.poem.detail.Poems;
import ca.rmen.android.palidamuerte.provider.poem.PoemCursor;
import ca.rmen.android.palidamuerte.ui.Font;
import ca.rmen.android.palidamuerte.ui.ViewHolder;

public class SearchResultPoemListCursorAdapter extends CursorAdapter {

    private static final String TAG = Constants.TAG + SearchResultPoemListCursorAdapter.class.getSimpleName();

    private final Context mContext;
    private final String[] mSearchTerms;

    public SearchResultPoemListCursorAdapter(Context context, String[] searchTerms) {
        super(context, null, false);
        Log.v(TAG, "Constructor");
        mContext = context;
        mSearchTerms = searchTerms;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = View.inflate(context, R.layout.search_result_poem_title, null);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final PoemCursor cursorWrapper = (PoemCursor) cursor;
        TextView tvTitle = ViewHolder.get(view, R.id.title);
        tvTitle.setText(cursorWrapper.getTitle());
        tvTitle.setTypeface(Font.getTypeface(mContext));

        TextView tvMatchedText = ViewHolder.get(view, R.id.matched_text);
        CharSequence matchedText = getMatchedText(cursorWrapper);
        tvMatchedText.setText(matchedText);

        ImageView ivFavoriteIcon = ViewHolder.get(view, R.id.favoriteIcon);
        ivFavoriteIcon.setVisibility(cursorWrapper.getIsFavorite() ? View.VISIBLE : View.GONE);

        final long categoryId = cursorWrapper.getCategoryId();

        final TextView tvCategory = ViewHolder.get(view, R.id.category);
        tvCategory.setTag(cursorWrapper.getCategoryId());
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return Categories.getCategoryName(mContext, categoryId);
            }

            @Override
            protected void onPostExecute(String result) {
                if (tvCategory.getTag() == (Long) categoryId) tvCategory.setText(result);
            }
        }.execute();
    }

    /**
     * @return the text in the poem (or poem meta data) that contains the query terms
     */
    private CharSequence getMatchedText(PoemCursor cursor) {
        // Search in the main content
        CharSequence matchedText = Search.findContext(cursor.getContent(), mSearchTerms);
        if (!TextUtils.isEmpty(matchedText)) return matchedText;

        // Search in the pre-content
        matchedText = Search.findContext(cursor.getPreContent(), mSearchTerms);
        if (!TextUtils.isEmpty(matchedText)) return matchedText;

        // If the location matches, return the location/date string
        matchedText = Search.findContext(cursor.getLocation(), mSearchTerms);
        if (!TextUtils.isEmpty(matchedText)) return Poems.getLocationDateString(mContext, cursor);

        // If the year matches, return the location/date string
        for (String searchTerm : mSearchTerms) {
            if (TextUtils.isDigitsOnly(searchTerm) && Integer.valueOf(searchTerm) == cursor.getYear()) return Poems.getLocationDateString(mContext, cursor);
        }
        return null;
    }

}
