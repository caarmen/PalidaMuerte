package ca.rmen.android.palidamuerte.app;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import ca.rmen.android.palidamuerte.Constants;
import ca.rmen.android.palidamuerte.provider.poem.PoemCursor;

public class PoemListCursorAdapter extends CursorAdapter {

    private static final String TAG = Constants.TAG + PoemListCursorAdapter.class.getSimpleName();

    public PoemListCursorAdapter(Context context) {
        super(context, null, false);
        Log.v(TAG, "Constructor");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = new TextView(context);
        fillView(view, cursor);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        fillView(view, cursor);
    }

    private void fillView(View view, Cursor cursor) {
        PoemCursor cursorWrapper = (PoemCursor) cursor;
        TextView title = (TextView) view;
        title.setText(cursorWrapper.getTitle());
    }

}
