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
import ca.rmen.android.palidamuerte.provider.poem.PoemSelection;

public class Search {

    public static PoemSelection buildSelection(String searchQuery) {
        String[] keyWords = searchQuery.split(" ");

        PoemSelection poemSelection = new PoemSelection();

        // Search the year column for keywords that are numeric
        for (String keyWord : keyWords) {
            if (TextUtils.isDigitsOnly(keyWord)) {
                if (poemSelection.args() != null && poemSelection.args().length > 0) poemSelection.or();
                poemSelection.year(Integer.valueOf(keyWord));
            }
        }

        if (poemSelection.args() != null && poemSelection.args().length > 0) poemSelection.or();

        // Surround keywords with % to use with the LIKE query
        for (int i = 0; i < keyWords.length; i++)
            keyWords[i] = "%" + keyWords[i] + "%";

        poemSelection.locationLike(keyWords).or().titleLike(keyWords).or().preContentLike(keyWords).or().contentLike(keyWords);

        return poemSelection;
    }

}
