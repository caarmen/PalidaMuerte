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
