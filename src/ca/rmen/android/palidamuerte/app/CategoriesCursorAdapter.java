package ca.rmen.android.palidamuerte.app;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import ca.rmen.android.palidamuerte.Constants;
import ca.rmen.android.palidamuerte.R;
import ca.rmen.android.palidamuerte.provider.category.CategoryCursor;

public class CategoriesCursorAdapter extends CursorAdapter {
    private static final String TAG = Constants.TAG + CategoriesCursorAdapter.class.getSimpleName();

    private final Context mContext;

    public CategoriesCursorAdapter(Context context) {
        super(context, null, false);
        Log.v(TAG, "Constructor");
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = View.inflate(context, R.layout.category_title, null);
        fillView(view, cursor);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        fillView(view, cursor);
    }

    private void fillView(View view, Cursor cursor) {
        CategoryCursor cursorWrapper = (CategoryCursor) cursor;
        TextView title = (TextView) view.findViewById(R.id.title);
        String categoryName = cursorWrapper.getCategoryName();
        int categoryResId = mContext.getResources().getIdentifier(categoryName, "string", R.class.getPackage().getName());
        title.setText(mContext.getString(categoryResId));
    }
}
