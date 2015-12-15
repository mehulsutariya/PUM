package pl.polsl.pum2.shoppingapp.gui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.ShoppingList;


public class ListEditorFragment extends BaseRealmFragment {

    private static final String MESSAGE_DIALOG_TAG = "pl.polsl.pum2.shoppingapp.gui.ListEditorFragment.messageDialogTag";
    OnFragmentInteractionListener listener;
    private EditText listName;

    public ListEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_editor, container, false);
        listName = (EditText) view.findViewById(R.id.shopping_list_name);
        Button newMarketMapButton = (Button) view.findViewById(R.id.newMarketMapButton);
        newMarketMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrEditMarketMap();
            }
        });
        Button editMarketMapButton = (Button) view.findViewById(R.id.editMarketMapButton);
        editMarketMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrEditMarketMap();
            }
        });
        Button doneButton = (Button) view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formIsCompleted()) {
                    saveList();
                    getActivity().finish();
                } else {
                    showMessageDialog();
                }
            }
        });
        Button addProductsButton = (Button) view.findViewById(R.id.add_products_button);
        addProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formIsCompleted()) {
                    saveList();
                    ShoppingListEditorActivity activity = (ShoppingListEditorActivity) getActivity();
                    activity.setListName(listName.getText().toString());
                    listener.onEditList();
                } else {
                    showMessageDialog();
                }
            }
        });
        return view;
    }

    private void saveList() {
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ShoppingList list = realm.createObject(ShoppingList.class);
                    list.setName(listName.getText().toString());
                }
            });
        } catch (RealmPrimaryKeyConstraintException e) {
            //TODO
        }

    }

    private void createOrEditMarketMap() {
        Intent intent = new Intent(getActivity(), MarketMapEditorActivity.class);
        getActivity().startActivity(intent);
    }

    private void showMessageDialog() {
        MessageDialogFragment messageDialogFragment = new MessageDialogFragment();
        Bundle message = new Bundle();
        message.putString(MessageDialogFragment.MESSAGE, getString(R.string.new_list_form_message));
        messageDialogFragment.setArguments(message);
        messageDialogFragment.show(getActivity().getSupportFragmentManager(), MESSAGE_DIALOG_TAG);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    boolean formIsCompleted() {
        //TODO: sprawdzanie stanu spinnera
        return listName.length() != 0;
    }

    public interface OnFragmentInteractionListener {
        void onEditList();
    }

}
