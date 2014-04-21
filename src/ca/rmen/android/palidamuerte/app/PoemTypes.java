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
package ca.rmen.android.palidamuerte.app;

import android.content.Context;
import ca.rmen.android.palidamuerte.R;
import ca.rmen.android.palidamuerte.provider.poem_type.PoemTypeCursor;
import ca.rmen.android.palidamuerte.provider.poem_type.PoemTypeSelection;

public class PoemTypes {

    public static String getPoemTypeName(Context context, long poemTypeId) {
        PoemTypeCursor cursor = new PoemTypeSelection().id(poemTypeId).query(context.getContentResolver());
        try {
            if (!cursor.moveToFirst()) return null;
            String poemTypeResIdName = cursor.getPoemTypeName();
            int poemTypeResId = context.getResources().getIdentifier(poemTypeResIdName, "string", R.class.getPackage().getName());
            return context.getString(poemTypeResId);
        } finally {
            cursor.close();
        }
    }

}
