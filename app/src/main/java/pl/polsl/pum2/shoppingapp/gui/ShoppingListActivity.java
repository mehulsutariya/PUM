package pl.polsl.pum2.shoppingapp.gui;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.model.ShoppingListItemData;

public class ShoppingListActivity extends AppCompatActivity implements DeleteItemDialogFragment.DeleteItemDialogListener {

    private ShoppingListAdapter shoppingListAdapter;
    RecyclerView shoppingListRecyclerView;
    private List<ShoppingListItemData> shoppingListItemDataArray;
    private FloatingActionButton fab;
    private int positionOfItemToDelete;
    private int numberOfPositionsInEditMode;
    CoordinatorLayout.LayoutParams fabLayoutParams;
    CoordinatorLayout.Behavior defaultFABBehavior;
    CoordinatorLayout.Behavior scrollFABBehavior;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        setToolbar();
        setFloatingActionButton();
        setRecyclerView();
        numberOfPositionsInEditMode = 0;
    }

    private void setRecyclerView() {
        shoppingListRecyclerView = (RecyclerView) findViewById(R.id.shopping_list_recycler_view);
        shoppingListRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager shoppingListLayoutManager;
        shoppingListLayoutManager = new LinearLayoutManager(this);
        shoppingListRecyclerView.setLayoutManager(shoppingListLayoutManager);

        setRecyclerViewAdapter();
    }

    private void setRecyclerViewAdapter() {
        shoppingListItemDataArray = new ArrayList<>();
        shoppingListAdapter = new ShoppingListAdapter(shoppingListItemDataArray, this);
        shoppingListAdapter.setOnItemClickListener(new ShoppingListAdapter.OnItemClickListener() {

            @Override
            public void onDeleteButton(int position) {
                DialogFragment newFragment = new DeleteItemDialogFragment();
                newFragment.show(getFragmentManager(), "deleteItemDialogTag");
                positionOfItemToDelete = position;
            }

            @Override
            public void onEditButton(int position) {
                enterEditMode();
            }

            @Override
            public void onDoneButton(int position) {
                exitEditMode();
            }

            @Override
            public void onCancelButton(int position) {
                exitEditMode();
            }
        });
        shoppingListRecyclerView.setAdapter(shoppingListAdapter);
    }

    private void setFloatingActionButton() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Tymczasowo:
                ShoppingListItemData data = new ShoppingListItemData("nowy produkt", "Kategoria1", 1.0, 1, false);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        ShoppingListActivity.this, fab, "transition_add_new_items");
                ActivityCompat.startActivity(ShoppingListActivity.this, new Intent(ShoppingListActivity.this, AddNewItemsActivity.class),
                        options.toBundle());
                shoppingListItemDataArray.add(data);
                shoppingListAdapter.notifyItemInserted(shoppingListItemDataArray.size() - 1);
                Snackbar.make(view, getString(R.string.product_added), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        setFABLayoutParams();
    }

    private void setFABLayoutParams() {
        fabLayoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        defaultFABBehavior = fabLayoutParams.getBehavior();
        scrollFABBehavior = new ScrollFABBehavior(this,null);
        fabLayoutParams.setBehavior(scrollFABBehavior);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void enterEditMode() {
        if (numberOfPositionsInEditMode == 0) {
            fabLayoutParams.setBehavior(defaultFABBehavior);
            fab.hide();
        }
        numberOfPositionsInEditMode++;
    }

    private void exitEditMode() {
        hideSoftKeyboard();
        numberOfPositionsInEditMode--;
        if (numberOfPositionsInEditMode == 0) {
            fab.show();
            fabLayoutParams.setBehavior(scrollFABBehavior);
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
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
    public void onDialogPositiveClick(DialogFragment dialog) {
        shoppingListItemDataArray.remove(positionOfItemToDelete);
        shoppingListAdapter.notifyItemRemoved(positionOfItemToDelete);
        fab.show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        fab.show();
    }
}
