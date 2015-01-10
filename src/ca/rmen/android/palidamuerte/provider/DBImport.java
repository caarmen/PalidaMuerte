/**
 * Copyright 2014 Carmen Alvarez
 *
 * This file is part of Pálida Muerte.
 *
 * Pálida Muerte is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Pálida Muerte is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pálida Muerte. If not, see <http://www.gnu.org/licenses/>.
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
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import ca.rmen.android.palidamuerte.Constants;
import ca.rmen.android.palidamuerte.provider.category.CategoryColumns;
import ca.rmen.android.palidamuerte.provider.poem.PoemColumns;
import ca.rmen.android.palidamuerte.provider.poem_type.PoemTypeColumns;
import ca.rmen.android.palidamuerte.provider.series.SeriesColumns;

class DBImport {
    private static final String TAG = Constants.TAG + DBImport.class.getSimpleName();

    private final Context mContext;

    DBImport(Context context) {
        mContext = context;
    }

    void doImport(SQLiteDatabase db) {
        Log.v(TAG, "doImport");
        try {
            InputStream is = mContext.getAssets().open("data.xls");
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setEncoding("iso-8859-1");
            Workbook wb = Workbook.getWorkbook(is, wbSettings);
            db.beginTransaction();
            delete(db, PoemColumns.TABLE_NAME);
            delete(db, SeriesColumns.TABLE_NAME);
            delete(db, CategoryColumns.TABLE_NAME);
            delete(db, PoemTypeColumns.TABLE_NAME);
            importSheet(db, wb, PoemTypeColumns.TABLE_NAME);
            importSheet(db, wb, CategoryColumns.TABLE_NAME);
            importSheet(db, wb, SeriesColumns.TABLE_NAME);
            importSheet(db, wb, PoemColumns.TABLE_NAME);
            db.setTransactionSuccessful();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (BiffException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            db.endTransaction();
        }
        Log.v(TAG, "doImport: done");
    }

    private void importSheet(SQLiteDatabase db, Workbook wb, String sheetName) {
        Log.v(TAG, "importSheet: sheetName= " + sheetName);
        ContentValues[] values = readSheet(wb, sheetName);
        insert(db, sheetName, values);
        Log.v(TAG, "importSheet: imported " + values.length + " rows");
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
                String columnName = columnNames.get(c);
                if (columnName.startsWith("#"))
                    continue;
                String cellData = row[c].getContents();
                cellData = clean(cellData);
                rowData.put(columnName, cellData);
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

    private void delete(SQLiteDatabase db, String table) {
        int deleted = db.delete(table, null, null);
        Log.v(TAG, "deleted " + deleted + " rows from " + table);
    }

    private void insert(SQLiteDatabase db, String table, ContentValues[] values) {
        Log.v(TAG, "insert: table = " + table + ", " + values.length + " values");
        for (ContentValues cv : values)
            db.insert(table, null, cv);
    }
}
