/**
 * Copyright 2014 Carmen Alvarez
 *
 * This file is part of P�lida Muerte.
 *
 * P�lida Muerte is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * P�lida Muerte is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with P�lida Muerte. If not, see <http://www.gnu.org/licenses/>.
 */
package ca.rmen.android.palidamuerte;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ca.rmen.android.palidamuerte.app.Categories;
import ca.rmen.android.palidamuerte.app.PoemPagerAdapter;
import ca.rmen.android.palidamuerte.app.Poems;

/**
 * An activity representing a single poem detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link PoemListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link PoemDetailFragment}.
 */
public class PoemDetailActivity extends FragmentActivity { // NO_UCD (use default)

    private static final String TAG = Constants.TAG + PoemDetailActivity.class.getSimpleName();
    private PoemPagerAdapter mPoemPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem_detail);
        ca.rmen.android.palidamuerte.app.ActionBar.setCustomFont(this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        final long categoryId = getIntent().getLongExtra(PoemListActivity.EXTRA_CATEGORY_ID, -1);

        // If this is the first time we open the activity, we will use the meeting id provided in the intent.
        // If we are recreating the activity (because of a device rotation, for example), we will display the meeting that the user 
        // had previously swiped to, using the ViewPager.
        final long poemId;
        if (savedInstanceState != null) poemId = savedInstanceState.getLong(PoemDetailFragment.ARG_ITEM_ID);
        else
            poemId = getIntent().getLongExtra(PoemDetailFragment.ARG_ITEM_ID, -1);

        new AsyncTask<Void, Void, PoemPagerAdapter>() {

            private String mCategoryName;

            @Override
            protected PoemPagerAdapter doInBackground(Void... params) {
                mCategoryName = Categories.getCategoryName(PoemDetailActivity.this, categoryId);
                return new PoemPagerAdapter(PoemDetailActivity.this, categoryId, getSupportFragmentManager());
            }

            @Override
            protected void onPostExecute(PoemPagerAdapter result) {
                mPoemPagerAdapter = result;
                mViewPager.setAdapter(mPoemPagerAdapter);
                mViewPager.setOnPageChangeListener(mOnPageChangeListener);
                findViewById(R.id.activity_loading).setVisibility(View.GONE);
                int position = mPoemPagerAdapter.getPositionForPoem(poemId);
                mViewPager.setCurrentItem(position);
                getActionBar().setTitle(mCategoryName);
                String pageNumber = getString(R.string.page_number, position + 1, mPoemPagerAdapter.getCount());
                ((TextView) findViewById(R.id.page_number)).setText(pageNumber);
                invalidateOptionsMenu();
            }
        }.execute();

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        /*
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(PoemDetailFragment.ARG_ITEM_ID, getIntent().getLongExtra(PoemDetailFragment.ARG_ITEM_ID, -1));
            PoemDetailFragment fragment = new PoemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.poem_detail_container, fragment).commit();
        }
        */
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(TAG, "onSaveInstanceState: outState = " + outState);
        if (mPoemPagerAdapter != null) {
            long poemId = mPoemPagerAdapter.getPoemIdAt(mViewPager.getCurrentItem());
            outState.putLong(PoemDetailFragment.ARG_ITEM_ID, poemId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(TAG, "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.poem, menu);
        menu.findItem(R.id.action_about).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.v(TAG, "onPrepareOptionsMenu");
        super.onPrepareOptionsMenu(menu);
        MenuItem prev = menu.findItem(R.id.action_prev);
        MenuItem next = menu.findItem(R.id.action_next);
        prev.setVisible(true);
        next.setVisible(true);
        if (mPoemPagerAdapter != null) {
            prev.setEnabled(mViewPager.getCurrentItem() > 0);
            next.setEnabled(mViewPager.getCurrentItem() < mPoemPagerAdapter.getCount() - 1);
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
            Intent intent = new Intent(this, PoemListActivity.class);
            intent.putExtra(PoemListActivity.EXTRA_CATEGORY_ID, getIntent().getLongExtra(PoemListActivity.EXTRA_CATEGORY_ID, -1));
            NavUtils.navigateUpTo(this, intent);
            return true;
        } else if (id == R.id.action_share) {
            long poemId = mPoemPagerAdapter.getPoemIdAt(mViewPager.getCurrentItem());
            Poems.share(this, poemId);
        } else if (id == R.id.action_prev) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        } else if (id == R.id.action_next) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
        if (mPoemPagerAdapter != null) mPoemPagerAdapter.destroy();
    }

    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            Log.v(TAG, "onPageSelected, position = " + position + ", item =" + mViewPager.getCurrentItem());
            String pageNumber = getString(R.string.page_number, position + 1, mPoemPagerAdapter.getCount());
            ((TextView) findViewById(R.id.page_number)).setText(pageNumber);
            invalidateOptionsMenu();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageScrollStateChanged(int state) {}
    };
}
