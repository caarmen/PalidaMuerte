package ca.rmen.android.palidamuerte;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * An activity representing a list of poems. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PoemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a {@link PoemListFragment} and the item details
 * (if present) is a {@link PoemDetailFragment}.
 * <p>
 * This activity also implements the required {@link PoemListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class PoemListActivity extends FragmentActivity implements PoemListFragment.Callbacks { // NO_UCD (use default)

    public static final String EXTRA_CATEGORY_ID = "poem_category_id";
    private static final String TAG = Constants.TAG + PoemListActivity.class.getSimpleName();
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem_list);

        if (findViewById(R.id.poem_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((PoemListFragment) getSupportFragmentManager().findFragmentById(R.id.poem_list)).setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link PoemListFragment.Callbacks} indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(long id) {
        Log.v(TAG, "onItemSelected: id = " + id);
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(PoemDetailFragment.ARG_ITEM_ID, id);
            PoemDetailFragment fragment = new PoemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.poem_detail_container, fragment).commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, PoemDetailActivity.class);
            detailIntent.putExtra(PoemDetailFragment.ARG_ITEM_ID, id);
            detailIntent.putExtra(EXTRA_CATEGORY_ID, getIntent().getLongExtra(EXTRA_CATEGORY_ID, -1));
            startActivity(detailIntent);
        }
    }
}
