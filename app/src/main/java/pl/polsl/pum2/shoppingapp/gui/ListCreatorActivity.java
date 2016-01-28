package pl.polsl.pum2.shoppingapp.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.MarketMap;
import pl.polsl.pum2.shoppingapp.database.ShoppingList;


public class ListCreatorActivity extends AppCompatActivity {

    private static final String MESSAGE_DIALOG_TAG = "pl.polsl.pum2.shoppingapp.gui.ListCreatorActivity.messageDialogTag";
    private EditText listName;
    private Realm realm;
    private MarketMap selectedMap;
    private Spinner marketMapSpinner;
    private CheckBox noMapCheckBox;
    private Button editMarketMapButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_creator);
        realm = Realm.getDefaultInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listName = (EditText) findViewById(R.id.shopping_list_name);
        setupButtons();
        setupSpinner();
        noMapCheckBox = (CheckBox) findViewById(R.id.no_map_check_box);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ListEditorActivity.EDIT_NEW_LIST) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }

    private void setupButtons() {
        Button newMarketMapButton = (Button) findViewById(R.id.newMarketMapButton);
        newMarketMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrEditMarketMap(false);
            }
        });
        editMarketMapButton = (Button) findViewById(R.id.editMarketMapButton);
        editMarketMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrEditMarketMap(true);
            }
        });
        Button doneButton = (Button) findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formIsCompleted()) {
                    try {
                        saveList();
                        finish();
                    } catch (RealmPrimaryKeyConstraintException e) {
                        showExistingListMessageDialog();
                    }
                } else {
                    showIncompleteFormMessageDialog();
                }
            }
        });
        Button addProductsButton = (Button) findViewById(R.id.add_products_button);
        addProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formIsCompleted()) {
                    try {
                        saveList();
                        Intent intent = new Intent(ListCreatorActivity.this, ListEditorActivity.class);
                        intent.putExtra(ListEditorActivity.LIST_NAME, listName.getText().toString().trim());
                        startActivityForResult(intent, ListEditorActivity.EDIT_NEW_LIST);
                    } catch (RealmPrimaryKeyConstraintException e) {
                        showExistingListMessageDialog();
                    }
                } else {
                    showIncompleteFormMessageDialog();
                }
            }
        });
    }

    private void setupSpinner() {
        marketMapSpinner = (Spinner) findViewById(R.id.maret_map_spinner);
        RealmResults<MarketMap> marketMaps = realm.where(MarketMap.class).notEqualTo("name", "").findAll();
        RealmSpinnerAdapter<MarketMap> spinnerAdapter = new RealmSpinnerAdapter<>(this, marketMaps, true);
        marketMapSpinner.setAdapter(spinnerAdapter);
        marketMapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMap = (MarketMap) parent.getSelectedItem();
                editMarketMapButton.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                editMarketMapButton.setEnabled(false);
            }
        });
        if (marketMapSpinner.getSelectedItem() == null) {
            editMarketMapButton.setEnabled(false);
        }
    }

    private void saveList() throws RealmPrimaryKeyConstraintException {
        realm.beginTransaction();
        try {
            ShoppingList list = realm.createObject(ShoppingList.class);
            list.setName(listName.getText().toString().trim());
            if (!noMapCheckBox.isChecked()) {
                list.setMarketMap(selectedMap);
            }
            realm.commitTransaction();
        } catch (RealmPrimaryKeyConstraintException primaryKeyException) {
            realm.cancelTransaction();
            throw primaryKeyException;
        }
    }

    private void showExistingListMessageDialog() {
        MessageDialogFragment dialogFragment = MessageDialogFragment.newInstance(getString(R.string.listAlreadyExistsMessage));
        dialogFragment.show(getSupportFragmentManager(), "listExistsMessage");
    }

    private void createOrEditMarketMap(boolean editMode) {
        Intent intent = new Intent(this, MarketMapEditorActivity.class);
        if (editMode) {
            intent.putExtra(MarketMapEditorActivity.MAP_NAME, ((MarketMap) marketMapSpinner.getSelectedItem()).getName());
        }
        startActivity(intent);
    }

    private void showIncompleteFormMessageDialog() {
        String message;
        if (noMapCheckBox.isChecked()) {
            message = (getString(R.string.new_list_form_message_without_map));
        } else {
            message = (getString(R.string.new_list_form_message));
        }
        MessageDialogFragment messageDialogFragment = MessageDialogFragment.newInstance(message);
        messageDialogFragment.show(getSupportFragmentManager(), MESSAGE_DIALOG_TAG);
    }

    boolean formIsCompleted() {
        if (noMapCheckBox.isChecked())
            return listName.length() != 0;
        return listName.length() != 0 && marketMapSpinner.getSelectedItem() != null;
    }

}
