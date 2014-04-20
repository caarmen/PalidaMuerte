/*
 * This file was generated by the Android ContentProvider Generator: https://github.com/BoD/android-contentprovider-generator
 */
package ca.rmen.android.palidamuerte.provider.poem;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import ca.rmen.android.palidamuerte.provider.base.AbstractSelection;

/**
 * Selection for the {@code poem} table.
 */
public class PoemSelection extends AbstractSelection<PoemSelection> {
    @Override
    public Uri uri() {
        return PoemColumns.CONTENT_URI;
    }
    
    /**
     * Query the given content resolver using this selection.
     * 
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort
     *            order, which may be unordered.
     * @return A {@code PoemCursor} object, which is positioned before the first entry, or null.
     */
    public PoemCursor query(ContentResolver contentResolver, String[] projection, String sortOrder) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), sortOrder);
        if (cursor == null) return null;
        return new PoemCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null}.
     */
    public PoemCursor query(ContentResolver contentResolver, String[] projection) {
        return query(contentResolver, projection, null);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null, null}.
     */
    public PoemCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null, null);
    }
    
    
    public PoemSelection id(long... value) {
        addEquals(PoemColumns._ID, toObjectArray(value));
        return this;
    }

    public PoemSelection poemTypeId(long... value) {
        addEquals(PoemColumns.POEM_TYPE_ID, toObjectArray(value));
        return this;
    }
    
    public PoemSelection poemTypeIdNot(long... value) {
        addNotEquals(PoemColumns.POEM_TYPE_ID, toObjectArray(value));
        return this;
    }

    public PoemSelection poemTypeIdGt(long value) {
        addGreaterThan(PoemColumns.POEM_TYPE_ID, value);
        return this;
    }

    public PoemSelection poemTypeIdGtEq(long value) {
        addGreaterThanOrEquals(PoemColumns.POEM_TYPE_ID, value);
        return this;
    }

    public PoemSelection poemTypeIdLt(long value) {
        addLessThan(PoemColumns.POEM_TYPE_ID, value);
        return this;
    }

    public PoemSelection poemTypeIdLtEq(long value) {
        addLessThanOrEquals(PoemColumns.POEM_TYPE_ID, value);
        return this;
    }

    public PoemSelection poemNumber(Integer... value) {
        addEquals(PoemColumns.POEM_NUMBER, value);
        return this;
    }
    
    public PoemSelection poemNumberNot(Integer... value) {
        addNotEquals(PoemColumns.POEM_NUMBER, value);
        return this;
    }

    public PoemSelection poemNumberGt(int value) {
        addGreaterThan(PoemColumns.POEM_NUMBER, value);
        return this;
    }

    public PoemSelection poemNumberGtEq(int value) {
        addGreaterThanOrEquals(PoemColumns.POEM_NUMBER, value);
        return this;
    }

    public PoemSelection poemNumberLt(int value) {
        addLessThan(PoemColumns.POEM_NUMBER, value);
        return this;
    }

    public PoemSelection poemNumberLtEq(int value) {
        addLessThanOrEquals(PoemColumns.POEM_NUMBER, value);
        return this;
    }

    public PoemSelection seriesId(long... value) {
        addEquals(PoemColumns.SERIES_ID, toObjectArray(value));
        return this;
    }
    
    public PoemSelection seriesIdNot(long... value) {
        addNotEquals(PoemColumns.SERIES_ID, toObjectArray(value));
        return this;
    }

    public PoemSelection seriesIdGt(long value) {
        addGreaterThan(PoemColumns.SERIES_ID, value);
        return this;
    }

    public PoemSelection seriesIdGtEq(long value) {
        addGreaterThanOrEquals(PoemColumns.SERIES_ID, value);
        return this;
    }

    public PoemSelection seriesIdLt(long value) {
        addLessThan(PoemColumns.SERIES_ID, value);
        return this;
    }

    public PoemSelection seriesIdLtEq(long value) {
        addLessThanOrEquals(PoemColumns.SERIES_ID, value);
        return this;
    }

    public PoemSelection categoryId(long... value) {
        addEquals(PoemColumns.CATEGORY_ID, toObjectArray(value));
        return this;
    }
    
    public PoemSelection categoryIdNot(long... value) {
        addNotEquals(PoemColumns.CATEGORY_ID, toObjectArray(value));
        return this;
    }

    public PoemSelection categoryIdGt(long value) {
        addGreaterThan(PoemColumns.CATEGORY_ID, value);
        return this;
    }

    public PoemSelection categoryIdGtEq(long value) {
        addGreaterThanOrEquals(PoemColumns.CATEGORY_ID, value);
        return this;
    }

    public PoemSelection categoryIdLt(long value) {
        addLessThan(PoemColumns.CATEGORY_ID, value);
        return this;
    }

    public PoemSelection categoryIdLtEq(long value) {
        addLessThanOrEquals(PoemColumns.CATEGORY_ID, value);
        return this;
    }

    public PoemSelection location(String... value) {
        addEquals(PoemColumns.LOCATION, value);
        return this;
    }
    
    public PoemSelection locationNot(String... value) {
        addNotEquals(PoemColumns.LOCATION, value);
        return this;
    }


    public PoemSelection year(int... value) {
        addEquals(PoemColumns.YEAR, toObjectArray(value));
        return this;
    }
    
    public PoemSelection yearNot(int... value) {
        addNotEquals(PoemColumns.YEAR, toObjectArray(value));
        return this;
    }

    public PoemSelection yearGt(int value) {
        addGreaterThan(PoemColumns.YEAR, value);
        return this;
    }

    public PoemSelection yearGtEq(int value) {
        addGreaterThanOrEquals(PoemColumns.YEAR, value);
        return this;
    }

    public PoemSelection yearLt(int value) {
        addLessThan(PoemColumns.YEAR, value);
        return this;
    }

    public PoemSelection yearLtEq(int value) {
        addLessThanOrEquals(PoemColumns.YEAR, value);
        return this;
    }

    public PoemSelection month(int... value) {
        addEquals(PoemColumns.MONTH, toObjectArray(value));
        return this;
    }
    
    public PoemSelection monthNot(int... value) {
        addNotEquals(PoemColumns.MONTH, toObjectArray(value));
        return this;
    }

    public PoemSelection monthGt(int value) {
        addGreaterThan(PoemColumns.MONTH, value);
        return this;
    }

    public PoemSelection monthGtEq(int value) {
        addGreaterThanOrEquals(PoemColumns.MONTH, value);
        return this;
    }

    public PoemSelection monthLt(int value) {
        addLessThan(PoemColumns.MONTH, value);
        return this;
    }

    public PoemSelection monthLtEq(int value) {
        addLessThanOrEquals(PoemColumns.MONTH, value);
        return this;
    }

    public PoemSelection day(int... value) {
        addEquals(PoemColumns.DAY, toObjectArray(value));
        return this;
    }
    
    public PoemSelection dayNot(int... value) {
        addNotEquals(PoemColumns.DAY, toObjectArray(value));
        return this;
    }

    public PoemSelection dayGt(int value) {
        addGreaterThan(PoemColumns.DAY, value);
        return this;
    }

    public PoemSelection dayGtEq(int value) {
        addGreaterThanOrEquals(PoemColumns.DAY, value);
        return this;
    }

    public PoemSelection dayLt(int value) {
        addLessThan(PoemColumns.DAY, value);
        return this;
    }

    public PoemSelection dayLtEq(int value) {
        addLessThanOrEquals(PoemColumns.DAY, value);
        return this;
    }

    public PoemSelection title(String... value) {
        addEquals(PoemColumns.TITLE, value);
        return this;
    }
    
    public PoemSelection titleNot(String... value) {
        addNotEquals(PoemColumns.TITLE, value);
        return this;
    }


    public PoemSelection preContent(String... value) {
        addEquals(PoemColumns.PRE_CONTENT, value);
        return this;
    }
    
    public PoemSelection preContentNot(String... value) {
        addNotEquals(PoemColumns.PRE_CONTENT, value);
        return this;
    }


    public PoemSelection content(String... value) {
        addEquals(PoemColumns.CONTENT, value);
        return this;
    }
    
    public PoemSelection contentNot(String... value) {
        addNotEquals(PoemColumns.CONTENT, value);
        return this;
    }

}