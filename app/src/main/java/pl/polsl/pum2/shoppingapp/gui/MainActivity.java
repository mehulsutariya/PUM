package pl.polsl.pum2.shoppingapp.gui;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import pl.polsl.pum2.shoppingapp.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ShoppingListsFragment shoppingListsFragment;
    ProductsFragment productsFragment;
    ProductCategoriesFragment productCategoriesFragment;
    MarketMapsFragment marketMapsFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    int navigationViewCheckedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (savedInstanceState != null) {
            navigationViewCheckedItem = savedInstanceState.getInt("navigationViewCheckedItem");
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
                Intent intent = new Intent(getApplicationContext(), CreateOrEditListActivity.class);
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
        shoppingListsFragment = (ShoppingListsFragment)fragmentManager.findFragmentByTag("shoppingListsFragment");
        if (shoppingListsFragment == null) {
            shoppingListsFragment = new ShoppingListsFragment();
        }
        productsFragment = (ProductsFragment)fragmentManager.findFragmentByTag("productsFragment");
        if (productsFragment == null) {
            productsFragment = new ProductsFragment();
        }
        productCategoriesFragment = (ProductCategoriesFragment)fragmentManager.findFragmentByTag("productCategoriesFragment");
        if (productCategoriesFragment == null) {
            productCategoriesFragment = new ProductCategoriesFragment();
        }
        marketMapsFragment = (MarketMapsFragment)fragmentManager.findFragmentByTag("marketMapsFragment");
        if (marketMapsFragment == null) {
            marketMapsFragment = new MarketMapsFragment();
        }
        setCurrentFragment();
    }

    private void setCurrentFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (navigationViewCheckedItem){
            case R.id.nav_shopping_lists:
                fragmentTransaction.replace(R.id.fragment_container, shoppingListsFragment, "shoppingListsFragment");
                break;
            case R.id.nav_products:
                fragmentTransaction.replace(R.id.fragment_container, productsFragment, "productsFragment");
                break;
            case R.id.nav_categories:
                fragmentTransaction.replace(R.id.fragment_container, productCategoriesFragment, "productCategoriesFragment");
                break;
            case R.id.nav_market_maps:
                fragmentTransaction.replace(R.id.fragment_container, marketMapsFragment, "marketMapsFragment");
                break;
        }
        fragmentTransaction.commit();
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
        outState.putInt("navigationViewCheckedItem", navigationViewCheckedItem);
    }
}
