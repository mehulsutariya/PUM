package pl.polsl.pum2.shoppingapp.gui;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.model.ShoppingListItemData;

public class ShoppingListActivity extends AppCompatActivity implements DeleteItemDialogFragment.DeleteItemDialogListener {

    private ShoppingListAdapter shoppingListAdapter;
    private List<ShoppingListItemData> shoppingListItemDataArray = new ArrayList<>();
    private FloatingActionButton fab;
    private int numberOfPositionsInEditMode = 0;
    private int positionOfItemToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Tymczasowo:
                ShoppingListItemData data = new ShoppingListItemData("nowy produkt", "Kategoria1", 1.0, false);
                Intent intent = new Intent(getApplicationContext(), AddNewItemsActivity.class);
                startActivity(intent);
                shoppingListItemDataArray.add(data);
                shoppingListAdapter.notifyItemInserted(shoppingListItemDataArray.size() - 1);
                Snackbar.make(view, getString(R.string.product_added), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RecyclerView shoppingListRecyclerView;
        shoppingListRecyclerView = (RecyclerView) findViewById(R.id.shopping_list_recycler_view);
        shoppingListRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager shoppingListLayoutManager;
        shoppingListLayoutManager = new LinearLayoutManager(this);
        shoppingListRecyclerView.setLayoutManager(shoppingListLayoutManager);

        shoppingListAdapter = new ShoppingListAdapter(shoppingListItemDataArray);
        shoppingListAdapter.setOnItemClickListener(new ShoppingListAdapter.OnItemClickListener() {

            @Override
            public void onDeleteButton(View view, int position) {
                DialogFragment newFragment = new DeleteItemDialogFragment();
                newFragment.show(getFragmentManager(), "deleteItemDialogTag");
                positionOfItemToDelete = position;
            }

            @Override
            public void onEditButton(View view, int position) {
                setEditMode();
            }

            @Override
            public void onDoneButton(View view, int position) {
                setNormalMode();
            }

            @Override
            public void onCancelButton(View view, int position) {
                setNormalMode();
            }
        });
        shoppingListRecyclerView.setAdapter(shoppingListAdapter);
    }

    private void setEditMode() {
        if(numberOfPositionsInEditMode == 0) {
            fab.hide();
        }
        numberOfPositionsInEditMode++;
    }

    private void setNormalMode() {
        numberOfPositionsInEditMode--;
        if (numberOfPositionsInEditMode == 0) {
            fab.show();
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
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
