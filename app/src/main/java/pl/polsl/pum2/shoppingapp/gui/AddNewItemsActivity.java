package pl.polsl.pum2.shoppingapp.gui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.model.ShoppingListItemData;

public class AddNewItemsActivity extends AppCompatActivity {

    private List<ShoppingListItemData> shoppingListItemDataArray = new ArrayList<>();
    private NewItemsListAdapter newItemsListAdapter;

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

        RecyclerView newItemsRecyclerView;
        newItemsRecyclerView = (RecyclerView)findViewById(R.id.new_items_list_recycler_view);
        newItemsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager newItemsListLayoutManager = new LinearLayoutManager(this);
        newItemsRecyclerView.setLayoutManager(newItemsListLayoutManager);
        newItemsListAdapter = new NewItemsListAdapter(shoppingListItemDataArray);
        newItemsRecyclerView.setAdapter(newItemsListAdapter);
        insertNewItem(0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void insertNewItem(int position) {
        shoppingListItemDataArray.add(new ShoppingListItemData());
        newItemsListAdapter.notifyItemInserted(position);
    }

    public void addItemButtonClick (View v) {
        //TODO
        //Tymczasowo:
        insertNewItem(shoppingListItemDataArray.size() - 1);
    }

}
