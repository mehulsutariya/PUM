package pl.polsl.pum2.shoppingapp.gui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.MarketMap;
import pl.polsl.pum2.shoppingapp.database.ProductCategory;

public class MarketMapEditorActivity extends AppCompatActivity implements ProductCategoryDialogFragment.ProductCategoryDialogListener {

    public static final String MAP_NAME = "mapName";
    private static final String MAP_EDITOR_DIALOG_TAG = "MapEditorDialogTag";
    private EditText mapNameEditText;
    private RecyclerView mapContentRecyclerView;
    private MarketMap map;
    private Realm realm;
    private MarketMapRecyclerViewAdapter recyclerViewAdapter;
    private boolean doneButtonClicked;
    private boolean isSavedInstance;
    private boolean isEditMode;
    private MarketMapEditorDataFragment dataFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_map_editor);
        realm = Realm.getDefaultInstance();
        mapNameEditText = (EditText) findViewById(R.id.map_name);

        Bundle intentExtras = getIntent().getExtras();
        String mapName;
        isEditMode = false;
        isSavedInstance = false;
        if (intentExtras != null) {
            mapName = intentExtras.getString(MAP_NAME, "");
            isEditMode = true;
        } else if (savedInstanceState != null) {
            mapName = savedInstanceState.getString(MAP_NAME);
            isSavedInstance = true;
        } else {
            mapName = "";
        }

        setupDataFragment();
        setupMarketMap(savedInstanceState, mapName);
        setupRecyclerView();

        doneButtonClicked = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            realm.beginTransaction();
            map.setName(mapNameEditText.getText().toString());
            realm.commitTransaction();
        } catch (RealmPrimaryKeyConstraintException e) {
            realm.cancelTransaction();
            Snackbar.make(mapContentRecyclerView, R.string.category_already_on_list, Snackbar.LENGTH_SHORT).show();
        }
        isSavedInstance = true;
        outState.putString(MAP_NAME, mapNameEditText.getText().toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!doneButtonClicked && !isSavedInstance) {
            if (isEditMode) {
                realm.beginTransaction();
                RealmList<ProductCategory> categoriesCopy = new RealmList<>();
                categoriesCopy.addAll(realm.copyToRealmOrUpdate(dataFragment.getOldProductCategories()));
                map.getProductCategories().clear();
                map.setProductCategories(categoriesCopy);
                realm.commitTransaction();
            } else {
                realm.beginTransaction();
                map.removeFromRealm();
                realm.commitTransaction();
            }
        }
        realm.close();
    }

    private void setupDataFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        dataFragment = (MarketMapEditorDataFragment) fragmentManager.findFragmentByTag("marketMapDataFragment");
        if (dataFragment == null) {
            dataFragment = new MarketMapEditorDataFragment();
            fragmentManager.beginTransaction().add(dataFragment, "marketMapDataFragment").commit();
        }
    }

    private void setupRecyclerView() {
        recyclerViewAdapter = new MarketMapRecyclerViewAdapter(this, map.getProductCategories());
        mapContentRecyclerView = (RecyclerView) findViewById(R.id.map_content_recycler_view);
        mapContentRecyclerView.setHasFixedSize(true);
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this);
        mapContentRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        mapContentRecyclerView.setAdapter(recyclerViewAdapter);
        setupRecyclerViewItemHelper();
    }

    private void setupRecyclerViewItemHelper() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int sourcePosition = viewHolder.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();
                RealmList<ProductCategory> productCategories = map.getProductCategories();
                realm.beginTransaction();
                ProductCategory previousCategory = productCategories.set(targetPosition, productCategories.get(sourcePosition));
                productCategories.set(sourcePosition, previousCategory);
                realm.commitTransaction();
                recyclerViewAdapter.notifyItemMoved(sourcePosition, targetPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });

        itemTouchHelper.attachToRecyclerView(mapContentRecyclerView);
    }

    private void setupMarketMap(Bundle savedInstanceState, String mapName) {
        if (mapName.length() > 0 || savedInstanceState != null) {
            mapNameEditText.append(mapName);
            map = realm.where(MarketMap.class).equalTo("name", mapName).findFirst();
        } else {
            realm.beginTransaction();
            try {
                map = realm.createObject(MarketMap.class);
            } catch (RealmPrimaryKeyConstraintException e) {
                MarketMap oldMap = realm.where(MarketMap.class).equalTo("name", "").findFirst();
                oldMap.removeFromRealm();
                map = realm.createObject(MarketMap.class);
            }
            realm.commitTransaction();
        }
        if (isEditMode && savedInstanceState == null) {
            dataFragment.setOldProductCategories(realm.copyFromRealm(map.getProductCategories()));
        }
    }

    public void doneButtonClick(View view) {
        if (mapNameEditText.length() == 0) {
            showDialog();
        } else {
            realm.beginTransaction();
            try {
                map.setName(mapNameEditText.getText().toString());
                realm.commitTransaction();
            } catch (RealmPrimaryKeyConstraintException e) {
                realm.cancelTransaction();

            }
            doneButtonClicked = true;
            finish();
        }
    }

    private void showDialog() {
        MessageDialogFragment dialogFragment = MessageDialogFragment.newInstance(getString(R.string.map_form_message));
        dialogFragment.show(getSupportFragmentManager(), MAP_EDITOR_DIALOG_TAG);
    }

    public void addButtonClick(View view) {
        ProductCategoryDialogFragment categoryDialogFragment = new ProductCategoryDialogFragment();
        categoryDialogFragment.show(getSupportFragmentManager(), "categoryDialog");
    }

    @Override
    public void onProductCategoryDialogOk(String categoryName) {
        insertNewOrExistingCategory(categoryName);
        recyclerViewAdapter.notifyItemInserted(map.getProductCategories().size() - 1);
    }

    private void insertNewOrExistingCategory(String categoryName) {
        RealmList<ProductCategory> productCategories = map.getProductCategories();
        realm.beginTransaction();
        try {
            ProductCategory newCategory = realm.createObject(ProductCategory.class);
            newCategory.setName(categoryName);
            productCategories.add(newCategory);
            realm.commitTransaction();
        } catch (RealmPrimaryKeyConstraintException e) {
            ProductCategory existingCategory = realm.where(ProductCategory.class).equalTo("name", categoryName).findFirst();
            realm.cancelTransaction();
            if (productCategories.contains(existingCategory)) {
                Snackbar.make(mapContentRecyclerView, R.string.category_already_on_list, Snackbar.LENGTH_SHORT).show();
            } else {
                realm.beginTransaction();
                productCategories.add(existingCategory);
                realm.commitTransaction();
            }
        }
    }
}
