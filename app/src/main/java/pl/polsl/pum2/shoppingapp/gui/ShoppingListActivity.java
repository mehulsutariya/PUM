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

    final static int SHOPPING_LIST = 0;
    final static int CART = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        setupToolbar();
        setupTabLayout();
        setupFloatingActionButton();
    }

    private void setupTabLayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.shopping_list_tab)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.shopping_cart_tab)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    showFloatingActionButton();
                } else {
                    fab.hide();
                }
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

    private void showFloatingActionButton() {
        fab.show();
        //korekta pozycji,
        //bez tego floating action button pojawia się w złym miejscu,
        // gdy został ukryty przy widocznym snackbarze
        fab.setTranslationY(0.0f);
    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupFloatingActionButton() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        ShoppingListActivity.this, fab, "transition_create_or_edit_list");
                Intent intent = new Intent(ShoppingListActivity.this, ShoppingListEditorActivity.class);
                intent.putExtra(ShoppingListEditorActivity.MODE, ShoppingListEditorActivity.ADD_LIST_ITEMS);
                ActivityCompat.startActivity(ShoppingListActivity.this, intent, options.toBundle());

                //TODO: Tymczasowo, później do usunięcia:--------------------------------------------------------
                ShoppingListItemData data = new ShoppingListItemData("nowy produkt", "Kategoria1", 1.0, 1, false);
                currentTabFragment = pagerAdapter.getCurrentFragment();
                if (currentTabFragment instanceof ShoppingListFragment) {
                    ShoppingListFragment fragment = (ShoppingListFragment) currentTabFragment;
                    fragment.addNewItem(data);
                }
                //------------------------------------------------------------------------------------------------

                Snackbar.make(view, getString(R.string.product_added), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        setFABLayoutParams();
    }

    private void setFABLayoutParams() {
        fabLayoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        defaultFABBehavior = fabLayoutParams.getBehavior();
        scrollFABBehavior = new ScrollFabBehavior();
        fabLayoutParams.setBehavior(scrollFABBehavior);
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
        showFloatingActionButton();
        fabLayoutParams.setBehavior(scrollFABBehavior);
    }

    @Override
    public void onItemRemoved() {
        showFloatingActionButton();
    }

    @Override
    public void onItemRemovingCanceled() {
        showFloatingActionButton();
    }

}
