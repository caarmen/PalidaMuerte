/*
 * This file was generated by the Android ContentProvider Generator: https://github.com/BoD/android-contentprovider-generator
 */
package ca.rmen.android.palidamuerte.provider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import ca.rmen.android.palidamuerte.BuildConfig;
import ca.rmen.android.palidamuerte.provider.category.CategoryColumns;
import ca.rmen.android.palidamuerte.provider.poem.PoemColumns;
import ca.rmen.android.palidamuerte.provider.poem_type.PoemTypeColumns;
import ca.rmen.android.palidamuerte.provider.series.SeriesColumns;

public class PalidaMuerteProvider extends ContentProvider {
    private static final String TAG = PalidaMuerteProvider.class.getSimpleName();

    private static final String TYPE_CURSOR_ITEM = "vnd.android.cursor.item/";
    private static final String TYPE_CURSOR_DIR = "vnd.android.cursor.dir/";

    public static final String AUTHORITY = BuildConfig.PROVIDER_AUTHORITY;
    public static final String CONTENT_URI_BASE = "content://" + AUTHORITY;

    public static final String QUERY_NOTIFY = "QUERY_NOTIFY";
    public static final String QUERY_GROUP_BY = "QUERY_GROUP_BY";

    private static final int URI_TYPE_CATEGORY = 0;
    private static final int URI_TYPE_CATEGORY_ID = 1;

    private static final int URI_TYPE_POEM = 2;
    private static final int URI_TYPE_POEM_ID = 3;

    private static final int URI_TYPE_POEM_TYPE = 4;
    private static final int URI_TYPE_POEM_TYPE_ID = 5;

    private static final int URI_TYPE_SERIES = 6;
    private static final int URI_TYPE_SERIES_ID = 7;



    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, CategoryColumns.TABLE_NAME, URI_TYPE_CATEGORY);
        URI_MATCHER.addURI(AUTHORITY, CategoryColumns.TABLE_NAME + "/#", URI_TYPE_CATEGORY_ID);
        URI_MATCHER.addURI(AUTHORITY, PoemColumns.TABLE_NAME, URI_TYPE_POEM);
        URI_MATCHER.addURI(AUTHORITY, PoemColumns.TABLE_NAME + "/#", URI_TYPE_POEM_ID);
        URI_MATCHER.addURI(AUTHORITY, PoemTypeColumns.TABLE_NAME, URI_TYPE_POEM_TYPE);
        URI_MATCHER.addURI(AUTHORITY, PoemTypeColumns.TABLE_NAME + "/#", URI_TYPE_POEM_TYPE_ID);
        URI_MATCHER.addURI(AUTHORITY, SeriesColumns.TABLE_NAME, URI_TYPE_SERIES);
        URI_MATCHER.addURI(AUTHORITY, SeriesColumns.TABLE_NAME + "/#", URI_TYPE_SERIES_ID);
    }

    private PalidaMuerteSQLiteOpenHelper mPalidaMuerteSQLiteOpenHelper;

    @Override
    public boolean onCreate() {
        if (BuildConfig.DEBUG) {
            // Enable logging of SQL statements as they are executed.
            try {
                Class<?> sqliteDebugClass = Class.forName("android.database.sqlite.SQLiteDebug");
                Field field = sqliteDebugClass.getDeclaredField("DEBUG_SQL_STATEMENTS");
                field.setAccessible(true);
                field.set(null, true);

                // Uncomment the following block if you also want logging of execution time (more verbose)
                // field = sqliteDebugClass.getDeclaredField("DEBUG_SQL_TIME");
                // field.setAccessible(true);
                // field.set(null, true);
            } catch (Throwable t) {
                if (BuildConfig.DEBUG) Log.w(TAG, "Could not enable SQLiteDebug logging", t);
            }
        }
        
        mPalidaMuerteSQLiteOpenHelper = PalidaMuerteSQLiteOpenHelper.newInstance(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case URI_TYPE_CATEGORY:
                return TYPE_CURSOR_DIR + CategoryColumns.TABLE_NAME;
            case URI_TYPE_CATEGORY_ID:
                return TYPE_CURSOR_ITEM + CategoryColumns.TABLE_NAME;

            case URI_TYPE_POEM:
                return TYPE_CURSOR_DIR + PoemColumns.TABLE_NAME;
            case URI_TYPE_POEM_ID:
                return TYPE_CURSOR_ITEM + PoemColumns.TABLE_NAME;

            case URI_TYPE_POEM_TYPE:
                return TYPE_CURSOR_DIR + PoemTypeColumns.TABLE_NAME;
            case URI_TYPE_POEM_TYPE_ID:
                return TYPE_CURSOR_ITEM + PoemTypeColumns.TABLE_NAME;

            case URI_TYPE_SERIES:
                return TYPE_CURSOR_DIR + SeriesColumns.TABLE_NAME;
            case URI_TYPE_SERIES_ID:
                return TYPE_CURSOR_ITEM + SeriesColumns.TABLE_NAME;

        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (BuildConfig.DEBUG) Log.d(TAG, "insert uri=" + uri + " values=" + values);
        String table = uri.getLastPathSegment();
        long rowId = mPalidaMuerteSQLiteOpenHelper.getWritableDatabase().insertOrThrow(table, null, values);
        if (rowId == -1) return null;
        String notify;
        if (rowId != -1 && ((notify = uri.getQueryParameter(QUERY_NOTIFY)) == null || "true".equals(notify))) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return uri.buildUpon().appendEncodedPath(String.valueOf(rowId)).build();
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (BuildConfig.DEBUG) Log.d(TAG, "bulkInsert uri=" + uri + " values.length=" + values.length);
        String table = uri.getLastPathSegment();
        SQLiteDatabase db = mPalidaMuerteSQLiteOpenHelper.getWritableDatabase();
        int res = 0;
        db.beginTransaction();
        try {
            for (ContentValues v : values) {
                long id = db.insert(table, null, v);
                db.yieldIfContendedSafely();
                if (id != -1) {
                    res++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        String notify;
        if (res != 0 && ((notify = uri.getQueryParameter(QUERY_NOTIFY)) == null || "true".equals(notify))) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return res;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, "update uri=" + uri + " values=" + values + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        QueryParams queryParams = getQueryParams(uri, selection);
        int res = mPalidaMuerteSQLiteOpenHelper.getWritableDatabase().update(queryParams.table, values, queryParams.selection, selectionArgs);
        String notify;
        if (res != 0 && ((notify = uri.getQueryParameter(QUERY_NOTIFY)) == null || "true".equals(notify))) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return res;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (BuildConfig.DEBUG) Log.d(TAG, "delete uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        QueryParams queryParams = getQueryParams(uri, selection);
        int res = mPalidaMuerteSQLiteOpenHelper.getWritableDatabase().delete(queryParams.table, queryParams.selection, selectionArgs);
        String notify;
        if (res != 0 && ((notify = uri.getQueryParameter(QUERY_NOTIFY)) == null || "true".equals(notify))) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return res;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String groupBy = uri.getQueryParameter(QUERY_GROUP_BY);
        if (BuildConfig.DEBUG)
            Log.d(TAG, "query uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs) + " sortOrder=" + sortOrder
                    + " groupBy=" + groupBy);
        QueryParams queryParams = getQueryParams(uri, selection);
        Cursor res = mPalidaMuerteSQLiteOpenHelper.getReadableDatabase().query(queryParams.table, projection, queryParams.selection, selectionArgs, groupBy,
                null, sortOrder == null ? queryParams.orderBy : sortOrder);
        res.setNotificationUri(getContext().getContentResolver(), uri);
        return res;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        HashSet<Uri> urisToNotify = new HashSet<Uri>(operations.size());
        for (ContentProviderOperation operation : operations) {
            urisToNotify.add(operation.getUri());
        }
        SQLiteDatabase db = mPalidaMuerteSQLiteOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            int numOperations = operations.size();
            ContentProviderResult[] results = new ContentProviderResult[numOperations];
            int i = 0;
            for (ContentProviderOperation operation : operations) {
                results[i] = operation.apply(this, results, i);
                if (operation.isYieldAllowed()) {
                    db.yieldIfContendedSafely();
                }
                i++;
            }
            db.setTransactionSuccessful();
            for (Uri uri : urisToNotify) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return results;
        } finally {
            db.endTransaction();
        }
    }

    private static class QueryParams {
        public String table;
        public String selection;
        public String orderBy;
    }

    private QueryParams getQueryParams(Uri uri, String selection) {
        QueryParams res = new QueryParams();
        String id = null;
        int matchedId = URI_MATCHER.match(uri);
        switch (matchedId) {
            case URI_TYPE_CATEGORY:
            case URI_TYPE_CATEGORY_ID:
                res.table = CategoryColumns.TABLE_NAME;
                res.orderBy = CategoryColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_POEM:
            case URI_TYPE_POEM_ID:
                res.table = PoemColumns.TABLE_NAME;
                res.orderBy = PoemColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_POEM_TYPE:
            case URI_TYPE_POEM_TYPE_ID:
                res.table = PoemTypeColumns.TABLE_NAME;
                res.orderBy = PoemTypeColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_SERIES:
            case URI_TYPE_SERIES_ID:
                res.table = SeriesColumns.TABLE_NAME;
                res.orderBy = SeriesColumns.DEFAULT_ORDER;
                break;

            default:
                throw new IllegalArgumentException("The uri '" + uri + "' is not supported by this ContentProvider");
        }

        switch (matchedId) {
            case URI_TYPE_CATEGORY_ID:
            case URI_TYPE_POEM_ID:
            case URI_TYPE_POEM_TYPE_ID:
            case URI_TYPE_SERIES_ID:
                id = uri.getLastPathSegment();
        }
        if (id != null) {
            if (selection != null) {
                res.selection = BaseColumns._ID + "=" + id + " and (" + selection + ")";
            } else {
                res.selection = BaseColumns._ID + "=" + id;
            }
        } else {
            res.selection = selection;
        }
        return res;
    }

    public static Uri notify(Uri uri, boolean notify) {
        return uri.buildUpon().appendQueryParameter(QUERY_NOTIFY, String.valueOf(notify)).build();
    }

    public static Uri groupBy(Uri uri, String groupBy) {
        return uri.buildUpon().appendQueryParameter(QUERY_GROUP_BY, groupBy).build();
    }
}
