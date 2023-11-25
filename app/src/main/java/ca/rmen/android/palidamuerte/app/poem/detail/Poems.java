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
package ca.rmen.android.palidamuerte.app.poem.detail;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.CursorAdapter;

import androidx.appcompat.widget.ShareActionProvider;

import java.util.Calendar;

import ca.rmen.android.palidamuerte.Constants;
import ca.rmen.android.palidamuerte.R;
import ca.rmen.android.palidamuerte.app.category.Categories;
import ca.rmen.android.palidamuerte.app.poem.list.FavoritePoemListCursorAdapter;
import ca.rmen.android.palidamuerte.app.poem.list.PoemListActivity;
import ca.rmen.android.palidamuerte.app.poem.list.PoemListCursorAdapter;
import ca.rmen.android.palidamuerte.app.poem.list.Search;
import ca.rmen.android.palidamuerte.app.poem.list.SearchResultPoemListCursorAdapter;
import ca.rmen.android.palidamuerte.provider.poem.PoemCursor;
import ca.rmen.android.palidamuerte.provider.poem.PoemSelection;

public class Poems {

    private static final String TAG = Constants.TAG + Poems.class.getSimpleName();

    public static void updateShareIntent(final ShareActionProvider shareActionProvider, final Context context, final long poemId) {
        new AsyncTask<Void, Void, Intent>() {

            @Override
            protected Intent doInBackground(Void... params) {
                PoemCursor cursor = new PoemSelection().id(poemId).query(context.getContentResolver());
                try {
                    if (!cursor.moveToFirst()) return null;
                    String subject = cursor.getTitle();
                    StringBuilder bodyBuilder = new StringBuilder();
                    bodyBuilder.append(cursor.getTitle()).append("\n\n");
                    String preContent = cursor.getPreContent();
                    if (!TextUtils.isEmpty(preContent)) bodyBuilder.append(preContent).append("\n\n");
                    bodyBuilder.append(cursor.getContent()).append("\n\n");
                    String poemNumberString = getPoemNumberString(context, cursor);
                    if (!TextUtils.isEmpty(poemNumberString)) bodyBuilder.append(poemNumberString).append("\n\n");
                    bodyBuilder.append(context.getString(R.string.copyright)).append("\n\n");
                    bodyBuilder.append(getLocationDateString(context, cursor));
                    String body = bodyBuilder.toString();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, body);
                    return sendIntent;

                } finally {
                    cursor.close();
                }
            }

            @Override
            protected void onPostExecute(Intent result) {
                shareActionProvider.setShareIntent(result);
            }

        }.execute();
    }

    public static String getLocationDateString(Context context, PoemCursor poemCursor) {
        int dateDisplayFlags = DateUtils.FORMAT_SHOW_DATE;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, poemCursor.getYear());
        calendar.set(Calendar.MONTH, poemCursor.getMonth() - 1);
        int day = poemCursor.getDay();
        // If the day is invalid, this means that we don't know
        // the day: we only know the year and the month.
        // We'll pretend that the day is 1 (this is required
        // for the month to be determined correctly), but
        // we won't display the day.
        if (day < 1) {
            day = 1;
            dateDisplayFlags = DateUtils.FORMAT_NO_MONTH_DAY;
        }

        calendar.set(Calendar.DAY_OF_MONTH, day);
        String dateString = DateUtils.formatDateTime(context, calendar.getTimeInMillis(), dateDisplayFlags);
        String locationDateString = String.format("%s, %s", poemCursor.getLocation(), dateString);
        return locationDateString;
    }

    static String getPoemNumberString(Context context, PoemCursor poemCursor) {
        Integer poemNumber = poemCursor.getPoemNumber();
        String poemTypeAndNumber = "";
        if (poemNumber != null) {
            String poemTypeName = PoemTypes.getPoemTypeName(context, poemCursor.getPoemTypeId());
            poemTypeAndNumber = context.getString(R.string.poem_type_and_number, poemTypeName, poemNumber);
        }
        return poemTypeAndNumber;
    }

    public static PoemSelection getPoemSelection(Context context, Intent intent) {
        intent.getExtras().isEmpty(); // so toString() will display the extras
        Log.v(TAG, "getPoemSelection, intent = " + intent + ", extras = " + intent.getExtras());
        final PoemSelection poemSelection;
        // Build a selection based on search keywords
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String queryString = intent.getStringExtra(SearchManager.QUERY);
            poemSelection = Search.buildSelection(queryString);
        } else {
            long categoryId = intent.getLongExtra(PoemListActivity.EXTRA_CATEGORY_ID, -1);
            poemSelection = new PoemSelection();
            // Build a selection which returns favorite poems
            if (categoryId == Categories.FAVORITE_CATEGORY_ID) poemSelection.isFavorite(true);
            // Build a selection which returns poems in the given category.
            else
                poemSelection.categoryId(categoryId);
        }
        return poemSelection;
    }

    public static CursorAdapter getPoemListAdapter(Context context, Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String queryString = intent.getStringExtra(SearchManager.QUERY);
            String[] searchTerms = Search.getSearchTerms(queryString);
            return new SearchResultPoemListCursorAdapter(context, searchTerms);
        }
        long categoryId = intent.getLongExtra(PoemListActivity.EXTRA_CATEGORY_ID, -1);
        if (categoryId == Categories.FAVORITE_CATEGORY_ID) return new FavoritePoemListCursorAdapter(context);
        else
            return new PoemListCursorAdapter(context);
    }

    public static String getActivityTitle(Context context, Intent intent) {
        long categoryId = intent.getLongExtra(PoemListActivity.EXTRA_CATEGORY_ID, -1);
        if (categoryId >= 0) return Categories.getCategoryName(context, categoryId);
        else if (intent.hasExtra(SearchManager.QUERY)) return context.getString(R.string.search_results);
        else
            return null;
    }
}
