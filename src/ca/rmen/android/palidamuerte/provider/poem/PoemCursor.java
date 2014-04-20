/*
 * This file was generated by the Android ContentProvider Generator: https://github.com/BoD/android-contentprovider-generator
 */
package ca.rmen.android.palidamuerte.provider.poem;

import java.util.Date;

import android.database.Cursor;

import ca.rmen.android.palidamuerte.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code poem} table.
 */
public class PoemCursor extends AbstractCursor {
    public PoemCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Get the {@code poem_type_id} value.
     */
    public long getPoemTypeId() {
        return getLongOrNull(PoemColumns.POEM_TYPE_ID);
    }

    /**
     * Get the {@code poem_number} value.
     * Can be {@code null}.
     */
    public Integer getPoemNumber() {
        return getIntegerOrNull(PoemColumns.POEM_NUMBER);
    }

    /**
     * Get the {@code series_id} value.
     */
    public long getSeriesId() {
        return getLongOrNull(PoemColumns.SERIES_ID);
    }

    /**
     * Get the {@code category_id} value.
     */
    public long getCategoryId() {
        return getLongOrNull(PoemColumns.CATEGORY_ID);
    }

    /**
     * Get the {@code location} value.
     * Cannot be {@code null}.
     */
    public String getLocation() {
        Integer index = getCachedColumnIndexOrThrow(PoemColumns.LOCATION);
        return getString(index);
    }

    /**
     * Get the {@code year} value.
     */
    public int getYear() {
        return getIntegerOrNull(PoemColumns.YEAR);
    }

    /**
     * Get the {@code month} value.
     */
    public int getMonth() {
        return getIntegerOrNull(PoemColumns.MONTH);
    }

    /**
     * Get the {@code day} value.
     */
    public int getDay() {
        return getIntegerOrNull(PoemColumns.DAY);
    }

    /**
     * Get the {@code title} value.
     * Cannot be {@code null}.
     */
    public String getTitle() {
        Integer index = getCachedColumnIndexOrThrow(PoemColumns.TITLE);
        return getString(index);
    }

    /**
     * Get the {@code pre_content} value.
     * Can be {@code null}.
     */
    public String getPreContent() {
        Integer index = getCachedColumnIndexOrThrow(PoemColumns.PRE_CONTENT);
        return getString(index);
    }

    /**
     * Get the {@code content} value.
     * Cannot be {@code null}.
     */
    public String getContent() {
        Integer index = getCachedColumnIndexOrThrow(PoemColumns.CONTENT);
        return getString(index);
    }
}