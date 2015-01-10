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
package ca.rmen.android.palidamuerte.app.poem.list;

import android.app.SearchManager;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import ca.rmen.android.palidamuerte.Constants;
import ca.rmen.android.palidamuerte.R;
import ca.rmen.android.palidamuerte.app.about.AboutActivity;
import ca.rmen.android.palidamuerte.app.category.list.CategoriesActivity;
import ca.rmen.android.palidamuerte.app.poem.detail.PoemDetailActivity;
import ca.rmen.android.palidamuerte.app.poem.detail.PoemDetailFragment;
import ca.rmen.android.palidamuerte.app.poem.detail.Poems;
import ca.rmen.android.palidamuerte.ui.ActionBar;

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
    private long mPoemId = -1;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem_list);
        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

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
        ActionBar.setCustomFont(this);

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
            boolean refreshMenu = mPoemId < 0;
            mPoemId = id;
            if (mShareActionProvider != null) Poems.updateShareIntent(mShareActionProvider, this, mPoemId);
            if (refreshMenu) invalidateOptionsMenu();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, PoemDetailActivity.class);
            detailIntent.setAction(getIntent().getAction());
            detailIntent.putExtra(SearchManager.QUERY, getIntent().getStringExtra(SearchManager.QUERY));
            detailIntent.putExtra(PoemDetailFragment.ARG_ITEM_ID, id);
            detailIntent.putExtra(EXTRA_CATEGORY_ID, getIntent().getLongExtra(EXTRA_CATEGORY_ID, -1));
            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(TAG, "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu);
        if (mTwoPane && mPoemId >= 0) {
            getMenuInflater().inflate(R.menu.menu_poem, menu);
            mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.action_share).getActionProvider();
            Poems.updateShareIntent(mShareActionProvider, this, mPoemId);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent intent = new Intent(this, CategoriesActivity.class);
            NavUtils.navigateUpTo(this, intent);
            return true;
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
