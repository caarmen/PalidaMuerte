package ca.rmen.android.palidamuerte.provider;

import java.io.IOException;

import jxl.read.biff.BiffException;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import ca.rmen.android.palidamuerte.BuildConfig;

public class PalidaMuerteSQLiteUpgradeHelper {
    private static final String TAG = PalidaMuerteSQLiteUpgradeHelper.class.getSimpleName();

    private static final String PREF_DB_IMPORTED = "db_imported";

    public void onUpgrade(final Context context, SQLiteDatabase db, int oldVersion, int newVersion) {
        if (BuildConfig.DEBUG) Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        // Insert your upgrading code here.
        // This file will not be overridden.
        if (oldVersion < 2) {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    DBImport dbImport = new DBImport(context);
                    try {
                        dbImport.doImport();
                    } catch (BiffException e) {
                        Log.e(TAG, e.getMessage(), e);
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                    return null;
                }
            }.execute();
        }
    }
}
