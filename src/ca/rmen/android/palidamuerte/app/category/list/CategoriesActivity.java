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
package ca.rmen.android.palidamuerte.app.category.list;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import ca.rmen.android.palidamuerte.R;

public class CategoriesActivity extends FragmentActivity { // NO_UCD (use default)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.logo);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        getActionBar().setCustomView(imageView);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
    }

}
