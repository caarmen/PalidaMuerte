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
package ca.rmen.android.palidamuerte.ui;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;
import ca.rmen.android.palidamuerte.Constants;

public class ActionBar {

    private static final String TAG = Constants.TAG + ActionBar.class.getSimpleName();

    public static void setCustomFont(Activity activity) {
        // http://stackoverflow.com/questions/8607707/how-to-set-a-custom-font-in-the-actionbar-title
        int titleId = activity.getResources().getIdentifier("action_bar_title", "id", "android");
        TextView tvActionBarTitle = (TextView) activity.findViewById(titleId);
        if (tvActionBarTitle != null) {
            Typeface typeface = Font.getTypeface(activity);
            tvActionBarTitle.setTypeface(typeface);
            tvActionBarTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvActionBarTitle.getTextSize() * 1.5f);
        }
    }

    /**
     * Hack workaround for bug where the ShareActionProvider takes up the whole height of the screen.
     * https://android-review.googlesource.com/#/c/37473/
     */
    public static void hackSetMaxHeight(final View actionView, final int maxHeight) {
        if (maxHeight == 0) return;// If we don't do this check, then when rotating to landscape and back to portrait, the bottom split action bar disappears.

        actionView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                final View actionViewParent = (View) actionView.getParent();
                if (actionViewParent != null && actionViewParent.getHeight() > maxHeight) {
                    Log.v(TAG, "hackSetMaxHeight: resizing view " + actionViewParent + " to height " + maxHeight);
                    actionView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    actionView.post(new Runnable() {

                        @Override
                        public void run() {
                            LayoutParams layoutParams = actionViewParent.getLayoutParams();
                            layoutParams.height = maxHeight;
                            actionViewParent.setLayoutParams(layoutParams);
                        }
                    });
                }
            }
        });
    }

}
