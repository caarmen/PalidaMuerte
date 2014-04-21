package ca.rmen.android.palidamuerte.app;

import android.content.Context;
import ca.rmen.android.palidamuerte.R;
import ca.rmen.android.palidamuerte.provider.poem_type.PoemTypeCursor;
import ca.rmen.android.palidamuerte.provider.poem_type.PoemTypeSelection;

public class PoemTypes {

    public static String getPoemTypeName(Context context, long poemTypeId) {
        PoemTypeCursor cursor = new PoemTypeSelection().id(poemTypeId).query(context.getContentResolver());
        try {
            if (!cursor.moveToFirst()) return null;
            String poemTypeResIdName = cursor.getPoemTypeName();
            int poemTypeResId = context.getResources().getIdentifier(poemTypeResIdName, "string", R.class.getPackage().getName());
            return context.getString(poemTypeResId);
        } finally {
            cursor.close();
        }
    }

}
