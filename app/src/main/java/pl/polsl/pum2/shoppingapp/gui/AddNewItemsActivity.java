package pl.polsl.pum2.shoppingapp.gui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Stack;

import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.model.ShoppingListItemData;

public class AddNewItemsActivity extends AppCompatActivity {

    private Stack<ShoppingListItemData> listItemDataStack = new Stack<>();
    private NewItemsListAdapter newItemsListAdapter;
    private RecyclerView newItemsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        newItemsRecyclerView = (RecyclerView)findViewById(R.id.new_items_list_recycler_view);
        newItemsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager newItemsListLayoutManager = new LinearLayoutManager(this);
        newItemsRecyclerView.setLayoutManager(newItemsListLayoutManager);
        newItemsListAdapter = new NewItemsListAdapter(listItemDataStack);
        newItemsRecyclerView.setAdapter(newItemsListAdapter);
        insertNewItem();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void insertNewItem() {
        listItemDataStack.push(new ShoppingListItemData());
        newItemsListAdapter.notifyItemInserted(0);
        newItemsRecyclerView.scrollToPosition(0);
    }

    public void addItemButtonClick (View v) {
        //TODO
        //Tymczasowo:
        insertNewItem();
    }

}
