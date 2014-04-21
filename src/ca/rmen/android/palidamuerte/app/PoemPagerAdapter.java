package ca.rmen.android.palidamuerte.app;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import ca.rmen.android.palidamuerte.Constants;
import ca.rmen.android.palidamuerte.PoemDetailFragment;
import ca.rmen.android.palidamuerte.provider.poem.PoemColumns;
import ca.rmen.android.palidamuerte.provider.poem.PoemCursor;

public class PoemPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = Constants.TAG + PoemPagerAdapter.class.getSimpleName();

    private PoemCursor mCursor;
    private final long mCategoryId;

    public PoemPagerAdapter(Context context, long categoryId, FragmentManager fm) {
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

    public int getPositionForPoem(long poemId) {
        if (mCursor.moveToFirst()) {
            do {
                if (mCursor.getId() == poemId) return mCursor.getPosition();
            } while (mCursor.moveToNext());
        }
        return -1;
    }

    public long getPoemIdAt(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getId();
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    public void destroy() {
        Log.v(TAG, "destroy");
        mCursor.close();
    }

}
