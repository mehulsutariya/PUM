package pl.polsl.pum2.shoppingapp.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.MarketMap;
import pl.polsl.pum2.shoppingapp.database.ProductCategory;

public class MarketMapEditorActivity extends AppCompatActivity implements ProductCategoryDialogFragment.ProductCategoryDialogListener {

    public static final String MAP_NAME = "mapName";
    private static final String MAP_EDITOR_DIALOG_TAG = "MapEditorDialogTag";
    private EditText mapNameEditText;
    private RealmRecyclerView mapContentRecyclerView;
    private RealmResults<ProductCategory> productCategories;
    private MarketMap map;
    private Realm realm;
    private BaseRecyclerViewRealmAdapter<ProductCategory> recyclerViewAdapter;
    private boolean doneButtonClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_map_editor);
        mapNameEditText = (EditText) findViewById(R.id.map_name);
        Bundle intentExtras = getIntent().getExtras();
        String mapName = "";
        if (intentExtras != null) {
            mapName = intentExtras.getString(MAP_NAME, "");
        }
        realm = Realm.getDefaultInstance();
        if (mapName.length() > 0) {
            mapNameEditText.setText(mapName);
            map = realm.where(MarketMap.class).equalTo("name", mapName).findFirst();
            productCategories = map.getProductCategories().where().findAll();
        } else {
            realm.beginTransaction();
            map = realm.createObject(MarketMap.class);
            realm.commitTransaction();
            productCategories = map.getProductCategories().where().findAll();
        }
        recyclerViewAdapter = new BaseRecyclerViewRealmAdapter<>(this, productCategories, true, true);

        mapContentRecyclerView = (RealmRecyclerView) findViewById(R.id.map_content_recycler_view);
        mapContentRecyclerView.setAdapter(recyclerViewAdapter);

        doneButtonClicked = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerViewAdapter.close();
        if (!doneButtonClicked) {
            realm.beginTransaction();
            map.removeFromRealm();
            realm.commitTransaction();
        }
        realm.close();
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
                //TODO:
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
        RealmList<ProductCategory> productCategories = map.getProductCategories();
        realm.beginTransaction();
        try {
            ProductCategory newCategory = realm.createObject(ProductCategory.class);
            newCategory.setName(categoryName);
            productCategories.add(newCategory);
            realm.commitTransaction();
        } catch (RealmPrimaryKeyConstraintException e) {
            ProductCategory existingCategory = realm.where(ProductCategory.class).equalTo("name", categoryName).findFirst();
            if (productCategories.contains(existingCategory)) {
                realm.cancelTransaction();
                //TODO
            } else {
                productCategories.add(existingCategory);
                realm.commitTransaction();
            }
        }
    }
}
