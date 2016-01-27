package pl.polsl.pum2.shoppingapp.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.MarketMap;
import pl.polsl.pum2.shoppingapp.database.Product;
import pl.polsl.pum2.shoppingapp.database.ProductCategory;
import pl.polsl.pum2.shoppingapp.database.ShoppingList;
import pl.polsl.pum2.shoppingapp.database.ShoppingListItem;

public class ListEditorActivity extends AppCompatActivity {

    public static final String LIST_NAME = "listName";
    public static final String NUMBER_OF_ITEMS = "itemsNumber";
    public static final String MODE = "mode";
    public static final int EDIT_EXISTING_LIST = 0;
    public static final int EDIT_NEW_LIST = 1;
    public static final int EDIT_SHOPPING_CART = 2;
    private static final String LAYOUT_MANAGER_STATE = "layoutManagerState";
    private static final String MESSAGE_DIALOG_TAG = "messageDialogTag";
    private List<ShoppingListItem> listItems;
    private ShoppingList shoppingList;
    private ListEditorAdapter listAdapter;
    private RecyclerView recyclerView;
    private int mode;

    private LinearLayoutManager recyclerViewLayoutManager;
    private Realm realm;
    private String listName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle intentExtras = getIntent().getExtras();
        listName = intentExtras.getString(LIST_NAME, "");
        mode = intentExtras.getInt(MODE, EDIT_EXISTING_LIST);
        realm = Realm.getDefaultInstance();
        shoppingList = realm.where(ShoppingList.class).equalTo("name", listName).findFirst();
        RealmResults<Product> products = realm.where(Product.class).findAll();
        MarketMap marketMap = shoppingList.getMarketMap();
        RealmResults<ProductCategory> productCategories;
        if (marketMap != null) {
            productCategories = marketMap.getProductCategories().where().findAll();
        } else {
            productCategories = null;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();

        ListEditorDataFragment dataFragment = (ListEditorDataFragment) fragmentManager.findFragmentByTag("dataFragment");
        if (dataFragment == null) {
            listItems = new ArrayList<>();
            dataFragment = new ListEditorDataFragment();
            fragmentManager.beginTransaction().add(dataFragment, "dataFragment").commit();
            dataFragment.setData(listItems);
        } else {
            listItems = dataFragment.getData();
        }

        recyclerView = (RecyclerView) findViewById(R.id.new_items_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        String mapName = null;
        if (marketMap != null) {
            mapName = marketMap.getName();
        }

        listAdapter = new ListEditorAdapter(this, listItems, products, productCategories, mapName);
        recyclerView.setAdapter(listAdapter);
        if (listItems.size() == 0) {
            insertNewItem();
        }
        setupButtons();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void insertNewItem() {
        ShoppingListItem item = new ShoppingListItem();
        if (mode == EDIT_SHOPPING_CART) {
            item.setBought(true);
        }
        listItems.add(item);
        listAdapter.notifyItemInserted(listItems.size() - 1);
        if (listItems.size() > 1) {
            recyclerView.scrollToPosition(listItems.size() - 1);
        }
    }

    private void setupButtons() {
        Button addNewItemButton = (Button) findViewById(R.id.add_new_item_button);
        addNewItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNewItem();
            }
        });
        Button doneButton = (Button) findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean formIsNotCompleted = false;
                for (ShoppingListItem item : listItems) {
                    if (itemIsEmpty(item)) {
                        formIsNotCompleted = true;
                        break;
                    }
                }
                if (formIsNotCompleted) {
                    showIncompleteFormMessageDialog();
                } else {
                    saveList();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(NUMBER_OF_ITEMS, listItems.size());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }
        });

    }

    private void saveList() {
        RealmList<ShoppingListItem> items = shoppingList.getItems();
        for (int i = 0; i < listItems.size(); i++) {
            ShoppingListItem item = listItems.get(i);
            try {
                realm.beginTransaction();
                items.add(item);
                realm.commitTransaction();
            } catch (RealmPrimaryKeyConstraintException e) {
                realm.cancelTransaction();
                String productName = item.getProduct().getName();
                Product existingProduct = realm.where(Product.class).equalTo("name", productName).findFirst();
                realm.beginTransaction();
                item.setProduct(existingProduct);
                items.add(item);
                realm.commitTransaction();
            }
        }
    }

    private boolean itemIsEmpty(ShoppingListItem item) {
        if (item.getProduct() == null || item.getQuantity() == 0 || item.getProduct().getName().equals("")) {
            return true;
        }
        return false;
    }

    private void showIncompleteFormMessageDialog() {
        MessageDialogFragment messageDialogFragment = MessageDialogFragment.newInstance(getString(R.string.new_items_fragment_message));
        messageDialogFragment.show(getSupportFragmentManager(), MESSAGE_DIALOG_TAG);
    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LAYOUT_MANAGER_STATE, recyclerViewLayoutManager.onSaveInstanceState());
    }
}
