package pl.polsl.pum2.shoppingapp.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.ShoppingList;


public class ListCreatorActivity extends AppCompatActivity {

    private static final String MESSAGE_DIALOG_TAG = "pl.polsl.pum2.shoppingapp.gui.ListCreatorActivity.messageDialogTag";
    private EditText listName;
    private Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_creator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listName = (EditText) findViewById(R.id.shopping_list_name);
        Button newMarketMapButton = (Button) findViewById(R.id.newMarketMapButton);
        newMarketMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrEditMarketMap();
            }
        });
        Button editMarketMapButton = (Button) findViewById(R.id.editMarketMapButton);
        editMarketMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrEditMarketMap();
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
        realm = Realm.getDefaultInstance();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void saveList() throws RealmPrimaryKeyConstraintException {
        realm.beginTransaction();
        try {
            ShoppingList list = realm.createObject(ShoppingList.class);
            list.setName(listName.getText().toString().trim());
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

    private void createOrEditMarketMap() {
        Intent intent = new Intent(this, MarketMapEditorActivity.class);
        startActivity(intent);
    }

    private void showIncompleteFormMessageDialog() {
        MessageDialogFragment messageDialogFragment = MessageDialogFragment.newInstance(getString(R.string.new_list_form_message));
        messageDialogFragment.show(getSupportFragmentManager(), MESSAGE_DIALOG_TAG);
    }

    boolean formIsCompleted() {
        //TODO: sprawdzanie stanu spinnera
        return listName.length() != 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ListEditorActivity.EDIT_NEW_LIST) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }

}
