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
package ca.rmen.android.palidamuerte.app.poem.list;

import android.text.TextUtils;
import ca.rmen.android.palidamuerte.provider.poem.PoemColumns;

public class Search {
    static class Query {
        String selection;
        String[] selectionArgs;
    }

    public static Query buildSelection(String searchQuery) {
        String[] searchColumns = new String[] { PoemColumns.LOCATION, PoemColumns.YEAR, PoemColumns.TITLE, PoemColumns.PRE_CONTENT, PoemColumns.CONTENT };
        String[] selections = new String[searchColumns.length];
        String[] keyWords = searchQuery.split(" ");
        for (int i = 0; i < searchColumns.length; i++) {
            selections[i] = buildSelection(keyWords, searchColumns[i]);
        }
        Query result = new Query();
        result.selection = TextUtils.join(" OR ", selections);
        int selectionArgCount = searchColumns.length * keyWords.length;
        String[] selectionArgs = new String[selectionArgCount];
        int i = 0;
        for (@SuppressWarnings("unused")
        String searchColumn : searchColumns)
            for (String keyWord : keyWords)
                selectionArgs[i++] = "%" + keyWord + "%";
        result.selectionArgs = selectionArgs;
        return result;
    }

    private static String buildSelection(String[] keyWords, String column) {
        String[] selections = new String[keyWords.length];
        for (int i = 0; i < keyWords.length; i++)
            selections[i] = column + " LIKE ?";
        String selection = TextUtils.join(" OR ", selections);
        return selection;
    }
}
