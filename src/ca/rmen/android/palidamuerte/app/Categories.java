package ca.rmen.android.palidamuerte.app;

import android.content.Context;
import ca.rmen.android.palidamuerte.R;
import ca.rmen.android.palidamuerte.provider.category.CategoryColumns;
import ca.rmen.android.palidamuerte.provider.category.CategoryCursor;
import ca.rmen.android.palidamuerte.provider.category.CategorySelection;

public class Categories {

    public static String getCategoryName(Context context, long categoryId) {
        CategoryCursor cursor = new CategorySelection().id(categoryId).query(context.getContentResolver(), new String[] { CategoryColumns.CATEGORY_NAME });
        try {
            if (!cursor.moveToFirst()) return null;
            String categoryResIdName = cursor.getCategoryName();
            int categoryResId = context.getResources().getIdentifier(categoryResIdName, "string", R.class.getPackage().getName());
            return context.getString(categoryResId);
        } finally {
            cursor.close();
        }
    }

}
