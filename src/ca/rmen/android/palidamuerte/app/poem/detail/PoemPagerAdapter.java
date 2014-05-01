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
package ca.rmen.android.palidamuerte.app.poem.detail;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import ca.rmen.android.palidamuerte.Constants;
import ca.rmen.android.palidamuerte.provider.poem.PoemColumns;
import ca.rmen.android.palidamuerte.provider.poem.PoemCursor;

class PoemPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = Constants.TAG + PoemPagerAdapter.class.getSimpleName();

    private PoemCursor mCursor;
    private final long mCategoryId;

    PoemPagerAdapter(Context context, long categoryId, FragmentManager fm) {
        super(fm);
        Log.v(TAG, "Constructor: categoryId = " + categoryId);
        mCategoryId = categoryId;
        Cursor cursor = context.getContentResolver().query(PoemColumns.CONTENT_URI, null, PoemColumns.CATEGORY_ID + "=?",
                new String[] { String.valueOf(mCategoryId) }, null);
        mCursor = new PoemCursor(cursor);
        mCursor.getCount();
    }

    @Override
    public Fragment getItem(int position) {
        Log.v(TAG, "getItem at position " + position);
        PoemDetailFragment fragment = new PoemDetailFragment();
        Bundle args = new Bundle(1);
        mCursor.moveToPosition(position);
        args.putLong(PoemDetailFragment.ARG_ITEM_ID, mCursor.getId());
        fragment.setArguments(args);
        return fragment;
    }

    int getPositionForPoem(long poemId) {
        if (mCursor.moveToFirst()) {
            do {
                if (mCursor.getId() == poemId) return mCursor.getPosition();
            } while (mCursor.moveToNext());
        }
        return -1;
    }

    long getPoemIdAt(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getId();
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    void destroy() {
        Log.v(TAG, "destroy");
        mCursor.close();
    }

}
