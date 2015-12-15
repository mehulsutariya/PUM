package pl.polsl.pum2.shoppingapp.gui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.Product;
import pl.polsl.pum2.shoppingapp.database.ShoppingList;
import pl.polsl.pum2.shoppingapp.database.ShoppingListItem;

public class ListItemsEditorFragment extends BaseRealmFragment {

    public static final String LIST_NAME = "listName";
    private static final String LAYOUT_MANAGER_STATE = "layoutManagerState";
    private static final String MESSAGE_DIALOG_TAG = "messageDialogTag";
    private List<ShoppingListItem> listItems;
    private ShoppingList shoppingList;
    private ListItemsEditorAdapter listAdapter;
    private RecyclerView recyclerView;
    private Activity activity;
    private View view;
    private LinearLayoutManager recyclerViewLayoutManager;

    public static ListItemsEditorFragment newInstance(String listName) {
        ListItemsEditorFragment fragment = new ListItemsEditorFragment();
        Bundle arguments = new Bundle();
        arguments.putString(LIST_NAME, listName);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        String listName = arguments.getString(LIST_NAME, "");
        shoppingList = realm.where(ShoppingList.class).equalTo("name", listName).findFirst();
        listItems = new ArrayList<>();
        setRetainInstance(true); //TODO: sprawdzić czy nadal potrzebne
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_items_editor, container, false);
        activity = getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.new_items_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(activity);

        if (savedInstanceState != null) {
            Parcelable state = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
            recyclerViewLayoutManager.onRestoreInstanceState(state);
        }
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        listAdapter = new ListItemsEditorAdapter(listItems);
        recyclerView.setAdapter(listAdapter);
        if (listItems.size() == 0) {
            insertNewItem();
        }
        setupButtons();
        return view;
    }

    private void insertNewItem() {
        listItems.add(new ShoppingListItem());
        listAdapter.notifyItemInserted(listItems.size() - 1);
        recyclerView.scrollToPosition(listItems.size() - 1);
    }

    private void setupButtons() {
        Button addNewItemButton = (Button) view.findViewById(R.id.add_new_item_button);
        addNewItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNewItem();
            }
        });
        Button doneButton = (Button) view.findViewById(R.id.done_button);
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
                    showMessageDialog();
                } else {
                    saveList();
                    activity.finish();
                }
            }
        });

    }

    private void saveList() {
        realm.beginTransaction();
        RealmList<ShoppingListItem> items = shoppingList.getItems();
        for (int i = 0; i < listItems.size(); i++) {
            ShoppingListItem item = listItems.get(i);
            try {
                items.add(item);
            } catch (RealmPrimaryKeyConstraintException e) {
                String productName = item.getProduct().getName();
                Product existingProduct = realm.where(Product.class).equalTo("name", productName).findFirst();
                item.setProduct(existingProduct);
                items.add(item);
            }
        }
        realm.copyToRealmOrUpdate(shoppingList);
        realm.commitTransaction();
    }

    private boolean itemIsEmpty(ShoppingListItem item) {
        //TODO
        return false;
    }

    private void showMessageDialog() {
        MessageDialogFragment messageDialogFragment = new MessageDialogFragment();
        Bundle message = new Bundle();
        message.putString(MessageDialogFragment.MESSAGE, getString(R.string.new_items_fragment_message));
        messageDialogFragment.setArguments(message);
        messageDialogFragment.show(getActivity().getSupportFragmentManager(), MESSAGE_DIALOG_TAG);
    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LAYOUT_MANAGER_STATE, recyclerViewLayoutManager.onSaveInstanceState());
    }
}
