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

import android.content.Context;
import ca.rmen.android.palidamuerte.R;
import ca.rmen.android.palidamuerte.provider.poem_type.PoemTypeCursor;
import ca.rmen.android.palidamuerte.provider.poem_type.PoemTypeSelection;

class PoemTypes {

    static String getPoemTypeName(Context context, long poemTypeId) {
        PoemTypeCursor cursor = new PoemTypeSelection().id(poemTypeId).query(context.getContentResolver());
        try {
            if (!cursor.moveToFirst()) return null;
            String poemTypeResIdName = cursor.getPoemTypeName();
            int poemTypeResId = context.getResources().getIdentifier(poemTypeResIdName, "string", context.getPackageName());
            return context.getString(poemTypeResId);
        } finally {
            cursor.close();
        }
    }

}
