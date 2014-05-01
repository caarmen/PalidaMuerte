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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import ca.rmen.android.palidamuerte.Constants;
import ca.rmen.android.palidamuerte.R;
import ca.rmen.android.palidamuerte.app.category.Categories;
import ca.rmen.android.palidamuerte.provider.poem.PoemCursor;
import ca.rmen.android.palidamuerte.ui.Font;

class FavoritePoemListCursorAdapter extends CursorAdapter {

    private static final String TAG = Constants.TAG + FavoritePoemListCursorAdapter.class.getSimpleName();

    private final Context mContext;

    FavoritePoemListCursorAdapter(Context context) {
        super(context, null, false);
        Log.v(TAG, "Constructor");
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = View.inflate(context, R.layout.favorite_poem_title, null);
        fillView(view, cursor);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        fillView(view, cursor);
    }

    private void fillView(View view, Cursor cursor) {
        PoemCursor cursorWrapper = (PoemCursor) cursor;
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        tvTitle.setText(cursorWrapper.getTitle());
        TextView tvCategory = (TextView) view.findViewById(R.id.category);
        String category = Categories.getCategoryName(mContext, cursorWrapper.getCategoryId());
        tvCategory.setText(category);
        tvTitle.setTypeface(Font.getTypeface(mContext));
    }

}
