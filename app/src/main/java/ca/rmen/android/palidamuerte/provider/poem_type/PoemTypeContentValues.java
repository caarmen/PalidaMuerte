/*
 * This file was generated by the Android ContentProvider Generator: https://github.com/BoD/android-contentprovider-generator
 */
package ca.rmen.android.palidamuerte.provider.poem_type;

import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;

import ca.rmen.android.palidamuerte.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code poem_type} table.
 */
public class PoemTypeContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return PoemTypeColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     * 
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, PoemTypeSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public PoemTypeContentValues putPoemTypeName(String value) {
        if (value == null) throw new IllegalArgumentException("value for poemTypeName must not be null");
        mContentValues.put(PoemTypeColumns.POEM_TYPE_NAME, value);
        return this;
    }


}