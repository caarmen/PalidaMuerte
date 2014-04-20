package ca.rmen.android.palidamuerte.provider;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ca.rmen.android.palidamuerte.BuildConfig;

public class PalidaMuerteSQLiteUpgradeHelper {
    private static final String TAG = PalidaMuerteSQLiteUpgradeHelper.class.getSimpleName();

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (BuildConfig.DEBUG) Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        // Insert your upgrading code here.
        // This file will not be overridden.
    }
}
