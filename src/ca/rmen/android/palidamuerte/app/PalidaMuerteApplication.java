package ca.rmen.android.palidamuerte.app;

import java.io.IOException;

import jxl.read.biff.BiffException;
import android.app.Application;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import ca.rmen.android.palidamuerte.Constants;
import ca.rmen.android.palidamuerte.provider.DBImport;
import ca.rmen.android.palidamuerte.provider.poem.PoemColumns;

public class PalidaMuerteApplication extends Application {
    private static final String TAG = Constants.TAG + PalidaMuerteApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                Cursor cursor = getContentResolver().query(PoemColumns.CONTENT_URI, new String[] { "count(*)" }, null, null, null);
                try {
                    if (cursor.moveToFirst() && cursor.getInt(0) > 0) {
                        Log.v(TAG, "No need to import");
                        //          return true;
                    }
                } finally {
                    cursor.close();
                }
                DBImport dbImport = new DBImport(PalidaMuerteApplication.this);
                try {
                    dbImport.doImport();
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
