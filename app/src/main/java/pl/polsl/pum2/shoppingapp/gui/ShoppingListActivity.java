package pl.polsl.pum2.shoppingapp.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import pl.polsl.pum2.shoppingapp.R;

public class ShoppingListActivity extends AppCompatActivity implements ShoppingListFragment.OnFragmentInteractionListener {
    public final static String LIST_NAME = "listName";
    private final static String EDIT_MODE_STATE = "editMode";
    private final static int EDITOR_ACTIVITY_REQUEST_CODE = 1;
    private FloatingActionButton fab;
    private TextView boughtProductsPriceSum;
    private CoordinatorLayout.LayoutParams fabLayoutParams;
    private CoordinatorLayout.Behavior defaultFABBehavior;
    private CoordinatorLayout.Behavior scrollFABBehavior;
    private String listName;
    private boolean editModeOn;
    private int currentListType;
    private boolean snackbarIsVisible;
    private SnackbarCallback snackbarCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        listName = getIntent().getStringExtra(LIST_NAME);
        if (listName == null) {
            listName = "";
        }
        boughtProductsPriceSum = (TextView) findViewById(R.id.price_sum);
        setupToolbar();
        setupTabLayout();
        setupFloatingActionButton();
        if (savedInstanceState != null) {
            editModeOn = savedInstanceState.getBoolean(EDIT_MODE_STATE);
        } else {
            editModeOn = false;
        }
        snackbarIsVisible = false;
        snackbarCallback = new SnackbarCallback();
    }

    private void setupTabLayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.shopping_list_tab)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.shopping_cart_tab)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ShoppingListPagerAdapter pagerAdapter = new ShoppingListPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), listName);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    currentListType = ShoppingListFragment.SHOPPING_LIST;
                    if (!editModeOn) {
                        showFloatingActionButton();
                    }
                    fabLayoutParams.setBehavior(scrollFABBehavior);
                } else {
                    currentListType = ShoppingListFragment.CART;
                    fab.hide();
                    fabLayoutParams.setBehavior(defaultFABBehavior);
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
        if (currentListType == ShoppingListFragment.SHOPPING_LIST) {
            fab.show();
            //korekta pozycji,
            //bez tego floating action button pojawia się w złym miejscu,
            // gdy został ukryty przy widocznym snackbarze
            if (!snackbarIsVisible) {
                fab.setTranslationY(0.0f);
            }
        }
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
                intent.putExtra(ShoppingListEditorActivity.LIST_NAME, listName);
                ActivityCompat.startActivityForResult(ShoppingListActivity.this, intent, EDITOR_ACTIVITY_REQUEST_CODE, options.toBundle());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Integer quantity = data.getIntExtra(ListItemsEditorFragment.NUMBER_OF_ITEMS, 0);
                if (quantity != null && quantity > 0) {
                    Snackbar.make(fab, getResources().getQuantityString(R.plurals.number_of_products_added, quantity, quantity), Snackbar.LENGTH_LONG).setCallback(snackbarCallback).show();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EDIT_MODE_STATE, editModeOn);
    }

    @Override
    public void onEnterEditMode() {
        if (currentListType == ShoppingListFragment.SHOPPING_LIST) {
            fab.hide();
            fabLayoutParams.setBehavior(defaultFABBehavior);
            editModeOn = true;
        }
    }

    @Override
    public void onExitEditMode() {
        if (currentListType == ShoppingListFragment.SHOPPING_LIST) {
            showFloatingActionButton();
            fabLayoutParams.setBehavior(scrollFABBehavior);
            editModeOn = false;
        }
    }

    @Override
    public void onItemRemoved() {
        showFloatingActionButton();
    }

    @Override
    public void onItemRemovingCanceled() {
        showFloatingActionButton();
    }

    @Override
    public void onItemBought() {
        Snackbar.make(fab, getString(R.string.product_bought), Snackbar.LENGTH_LONG).setCallback(snackbarCallback).show();
    }

    @Override
    public void onItemRestoredToList() {
        Snackbar.make(fab, getString(R.string.product_restored_to_list), Snackbar.LENGTH_LONG).setCallback(snackbarCallback).show();
    }

    @Override
    public void onBoughtItemsPriceSumChanged(String priceSum) {
        boughtProductsPriceSum.setText(priceSum);
    }

    private class SnackbarCallback extends Snackbar.Callback {
        @Override
        public void onDismissed(Snackbar snackbar, int event) {
            snackbarIsVisible = false;
        }

        @Override
        public void onShown(Snackbar snackbar) {
            snackbarIsVisible = true;
        }
    }

}
