package pl.polsl.pum2.shoppingapp.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.model.ShoppingListItemData;

public class ShoppingListActivity extends AppCompatActivity implements ShoppingListFragment.OnFragmentInteractionListener {

    private FloatingActionButton fab;
    private CoordinatorLayout.LayoutParams fabLayoutParams;
    private CoordinatorLayout.Behavior defaultFABBehavior;
    private CoordinatorLayout.Behavior scrollFABBehavior;
    private Fragment currentTabFragment;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        setToolbar();
        setTabLayout();
        setFloatingActionButton();
    }

    private void setTabLayout() {
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.shopping_list_tab)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.shopping_cart_tab)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setFloatingActionButton() {
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Tymczasowo:
                ShoppingListItemData data = new ShoppingListItemData("nowy produkt", "Kategoria1", 1.0, 1, false);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        ShoppingListActivity.this, fab, "transition_add_new_items");
                ActivityCompat.startActivity(ShoppingListActivity.this, new Intent(ShoppingListActivity.this, AddNewItemsActivity.class),
                        options.toBundle());
                currentTabFragment = pagerAdapter.getCurrentFragment();
                if(currentTabFragment instanceof ShoppingListFragment) {
                    ShoppingListFragment fragment = (ShoppingListFragment)currentTabFragment;
                    fragment.addNewItem(data);
                }
                Snackbar.make(view, getString(R.string.product_added), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        setFABLayoutParams();
    }

    private void setFABLayoutParams() {
        fabLayoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        defaultFABBehavior = fabLayoutParams.getBehavior();
        scrollFABBehavior = new ScrollFABBehavior(this, null);
        fabLayoutParams.setBehavior(scrollFABBehavior);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_shopping_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO zapisywanie stanu aktywnośći
    }

    @Override
    public void onEnterEditMode() {
        fab.hide();
        fabLayoutParams.setBehavior(defaultFABBehavior);
    }

    @Override
    public void onExitEditMode() {
        fab.show();
        fabLayoutParams.setBehavior(scrollFABBehavior);
    }

    @Override
    public void onItemRemoved() {
        fab.show();
    }

    @Override
    public void onItemRemovingCanceled() {
        fab.show();
    }

}
