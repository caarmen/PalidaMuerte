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
package ca.rmen.android.palidamuerte.provider;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import ca.rmen.android.palidamuerte.Constants;
import ca.rmen.android.palidamuerte.provider.category.CategoryColumns;
import ca.rmen.android.palidamuerte.provider.poem.PoemColumns;
import ca.rmen.android.palidamuerte.provider.poem_type.PoemTypeColumns;
import ca.rmen.android.palidamuerte.provider.series.SeriesColumns;

public class DBImport {
    private static final String TAG = Constants.TAG + DBImport.class.getSimpleName();
    public static final String PREF_DB_IMPORTED = "db_imported";

    private final Context mContext;

    public DBImport(Context context) {
        mContext = context;
    }

    public void doImport() throws IOException, BiffException {
        Log.v(TAG, "doImport");
        InputStream is = mContext.getAssets().open("data.xls");
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setEncoding("iso-8859-1");
        Workbook wb = Workbook.getWorkbook(is, wbSettings);
        importSheet(wb, PoemTypeColumns.TABLE_NAME, PoemTypeColumns.CONTENT_URI);
        importSheet(wb, CategoryColumns.TABLE_NAME, CategoryColumns.CONTENT_URI);
        importSheet(wb, SeriesColumns.TABLE_NAME, SeriesColumns.CONTENT_URI);
        importSheet(wb, PoemColumns.TABLE_NAME, PoemColumns.CONTENT_URI);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        sharedPrefs.edit().putBoolean(PREF_DB_IMPORTED, true).commit();
        Log.v(TAG, "doImport: done");
    }

    private void importSheet(Workbook wb, String sheetName, Uri uri) {
        Log.v(TAG, "importSheet: sheetName= " + sheetName + ", uri = " + uri);
        ContentValues[] values = readSheet(wb, sheetName);
        int result = insert(uri, values);
        Log.v(TAG, "importSheet: imported " + result + " rows");
    }

    private ContentValues[] readSheet(Workbook wb, String name) {
        Log.v(TAG, "readSheet: name = " + name);
        Sheet sheet = wb.getSheet(name);
        Cell[] headerRow = sheet.getRow(0);
        List<String> columnNames = new ArrayList<String>();
        for (Cell headerCell : headerRow)
            columnNames.add(headerCell.getContents());

        int columnCount = sheet.getColumns();
        int rowCount = sheet.getRows();
        ContentValues[] result = new ContentValues[rowCount - 1];
        for (int r = 1; r < rowCount; r++) {
            Cell[] row = sheet.getRow(r);
            ContentValues rowData = new ContentValues(columnCount);
            for (int c = 0; c < columnCount; c++) {
                String cellData = row[c].getContents();
                cellData = clean(cellData);
                rowData.put(columnNames.get(c), cellData);
            }
            result[r - 1] = rowData;
        }
        return result;
    }

    private String clean(String data) {
        if (data == null) return data;
        data = data.trim();
        data = data.replaceAll("\\\\n", "\n");
        if (TextUtils.isEmpty(data)) data = null;
        return data;
    }

    private int insert(Uri uri, ContentValues[] values) {
        Log.v(TAG, "insert: uri = " + uri + ", " + values.length + " values");
        int deleted = mContext.getContentResolver().delete(uri, null, null);
        Log.v(TAG, "deleted " + deleted + " rows before inserting");
        int result = mContext.getContentResolver().bulkInsert(uri, values);
        return result;
    }
}
