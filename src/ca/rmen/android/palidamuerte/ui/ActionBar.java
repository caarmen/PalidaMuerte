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
package ca.rmen.android.palidamuerte.ui;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.TextView;

public class ActionBar {

    public static void setCustomFont(Activity activity) {
        // http://stackoverflow.com/questions/8607707/how-to-set-a-custom-font-in-the-actionbar-title
        int titleId = activity.getResources().getIdentifier("action_bar_title", "id", "android");
        TextView tvActionBarTitle = (TextView) activity.findViewById(titleId);
        if (tvActionBarTitle != null) {
            Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "dancing_script.ttf");
            tvActionBarTitle.setTypeface(typeface);
            tvActionBarTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvActionBarTitle.getTextSize() * 1.5f);
        }
    }

}
