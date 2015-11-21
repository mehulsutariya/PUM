package pl.polsl.pum2.shoppingapp.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import pl.polsl.pum2.shoppingapp.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SHOPPING_LISTS_FRAGMENT = "shoppingListsFragment";
    private static final String PRODUCTS_FRAGMENT = "productsFragment";
    private static final String PRODUCT_CATEGORIES_FRAGMENT = "productCategoriesFragment";
    private static final String MARKET_MAPS_FRAGMENT = "marketMapsFragment";
    private static final String NAVIGATION_VIEW_CHECKED_ITEM = "navigationViewCheckedItem";
    private AllShoppingListsFragment allShoppingListsFragment;
    private ProductsFragment productsFragment;
    private ProductCategoriesFragment productCategoriesFragment;
    private MarketMapsFragment marketMapsFragment;
    private FragmentManager fragmentManager;
    private int navigationViewCheckedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (savedInstanceState != null) {
            navigationViewCheckedItem = savedInstanceState.getInt(NAVIGATION_VIEW_CHECKED_ITEM);
        } else {
            navigationViewCheckedItem = R.id.nav_shopping_lists;
        }
        setSupportActionBar(toolbar);
        setupFloatingActionButton();
        setupDrawerLayout(toolbar);
        setupFragments();
    }

    private void setupFloatingActionButton() {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShoppingListEditorActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this, fab, "transition_create_or_edit_list");
                ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
            }
        });
    }

    private void setupDrawerLayout(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(navigationViewCheckedItem);
    }

    private void setupFragments() {
        fragmentManager = getSupportFragmentManager();
        allShoppingListsFragment = (AllShoppingListsFragment) fragmentManager.findFragmentByTag(SHOPPING_LISTS_FRAGMENT);
        if (allShoppingListsFragment == null) {
            allShoppingListsFragment = new AllShoppingListsFragment();
        }
        productsFragment = (ProductsFragment) fragmentManager.findFragmentByTag(PRODUCTS_FRAGMENT);
        if (productsFragment == null) {
            productsFragment = new ProductsFragment();
        }
        productCategoriesFragment = (ProductCategoriesFragment) fragmentManager.findFragmentByTag(PRODUCT_CATEGORIES_FRAGMENT);
        if (productCategoriesFragment == null) {
            productCategoriesFragment = new ProductCategoriesFragment();
        }
        marketMapsFragment = (MarketMapsFragment) fragmentManager.findFragmentByTag(MARKET_MAPS_FRAGMENT);
        if (marketMapsFragment == null) {
            marketMapsFragment = new MarketMapsFragment();
        }
        setCurrentFragment();
    }

    private void setCurrentFragment() {
        Fragment newFragment = null;
        String tag = null;
        switch (navigationViewCheckedItem) {
            case R.id.nav_shopping_lists:
                newFragment = allShoppingListsFragment;
                tag = SHOPPING_LISTS_FRAGMENT;
                break;
            case R.id.nav_products:
                newFragment = productsFragment;
                tag = PRODUCTS_FRAGMENT;
                break;
            case R.id.nav_categories:
                newFragment = productCategoriesFragment;
                tag = PRODUCT_CATEGORIES_FRAGMENT;
                break;
            case R.id.nav_market_maps:
                newFragment = marketMapsFragment;
                tag = MARKET_MAPS_FRAGMENT;
                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, newFragment, tag)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        navigationViewCheckedItem = item.getItemId();
        setCurrentFragment();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAVIGATION_VIEW_CHECKED_ITEM, navigationViewCheckedItem);
    }
}
