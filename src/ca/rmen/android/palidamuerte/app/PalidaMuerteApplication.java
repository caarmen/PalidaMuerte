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
package ca.rmen.android.palidamuerte.app;

import java.io.IOException;

import jxl.read.biff.BiffException;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import ca.rmen.android.palidamuerte.Constants;
import ca.rmen.android.palidamuerte.provider.DBImport;

public class PalidaMuerteApplication extends Application { // NO_UCD (use default)
    private static final String TAG = Constants.TAG + PalidaMuerteApplication.class.getSimpleName();

    private static final String PREF_DB_IMPORTED = "db_imported";

    @Override
    public void onCreate() {
        super.onCreate();

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(PalidaMuerteApplication.this);
                if (sharedPrefs.getBoolean(PREF_DB_IMPORTED, false)) {
                    Log.v(TAG, "No need to import");
                    return true;
                }
                DBImport dbImport = new DBImport(PalidaMuerteApplication.this);
                try {
                    dbImport.doImport();
                    sharedPrefs.edit().putBoolean(PREF_DB_IMPORTED, true).commit();
                    return true;
                } catch (BiffException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (!result) {
                    Toast.makeText(PalidaMuerteApplication.this, "OMG", Toast.LENGTH_SHORT).show();
                }
            }

        }.execute();
    }
}
