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
import ca.rmen.android.palidamuerte.provider.poem.PoemSelection;

public class Search {

    private static final String ACCENTS = "‡Ž’—œ–çƒêîò„";
    private static final String NO_ACCENTS = "aeiounaeioun";

    public static String[] getSearchTerms(String queryString) {
        return TextUtils.split(queryString, " ");
    }

    public static PoemSelection buildSelection(String searchQuery) {
        String[] searchTerms = getSearchTerms(searchQuery);
        for (int i = 0; i < searchTerms.length; i++)
            searchTerms[i] = collateText(searchTerms[i]);

        PoemSelection poemSelection = new PoemSelection();

        // Search the year column for searchTerms that are numeric
        for (String searchTerm : searchTerms) {
            if (TextUtils.isDigitsOnly(searchTerm)) {
                if (poemSelection.args() != null && poemSelection.args().length > 0) poemSelection.or();
                poemSelection.year(Integer.valueOf(searchTerm));
            }
        }
        if (poemSelection.args() != null && poemSelection.args().length > 0) poemSelection.or();

        // Surround searchTerms with % to use with the LIKE query
        for (int i = 0; i < searchTerms.length; i++)
            searchTerms[i] = "%" + searchTerms[i] + "%";

        String[] columnNames = new String[] { PoemColumns.LOCATION, PoemColumns.TITLE, PoemColumns.PRE_CONTENT, PoemColumns.CONTENT };
        for (int i = 0; i < columnNames.length; i++) {
            for (int j = 0; j < searchTerms.length; j++)
                poemSelection.addRaw(collateColumn(columnNames[i]) + " LIKE ?", new String[] { searchTerms[j] });
            if (i < columnNames.length - 1) poemSelection.or();
        }

        return poemSelection;
    }

    private static String collateText(String searchTerm) {
        searchTerm = searchTerm.toLowerCase();
        for (int j = 0; j < ACCENTS.length(); j++)
            searchTerm = searchTerm.replace(ACCENTS.charAt(j), NO_ACCENTS.charAt(j));
        return searchTerm;
    }

    private static String collateColumn(String columnName) {
        String result = " LOWER(" + columnName + ")";
        for (int i = 0; i < ACCENTS.length(); i++)
            result = "replace(" + result + ",'" + ACCENTS.charAt(i) + "','" + NO_ACCENTS.charAt(i) + "')";
        return result;
    }

    public static String findContext(String content, String[] searchTerms) {
        String collatedContent = collateText(content);
        for (String searchTerm : searchTerms) {
            String collatedSearchTerm = collateText(searchTerm);
            String context = findContext(collatedContent, collatedSearchTerm);
            if (context != null) return context;
        }
        return null;
    }

    private static String findContext(String content, String searchTerm) {
        int i = content.indexOf(searchTerm);
        if (i < 0) return null;
        int begin = Math.max(0, i - 20);
        int end = Math.min(i + searchTerm.length() + 20, content.length());
        String result = content.substring(begin, end);
        if (begin > 0) result = "..." + result;
        if (end < content.length()) result = result + "...";
        result = result.replace("\n", " ");
        result = result.replace("\r", " ");
        result = result.replaceAll("  ", " ");
        return result;
    }

}
