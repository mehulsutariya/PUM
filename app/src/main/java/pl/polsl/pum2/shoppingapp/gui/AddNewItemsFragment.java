package pl.polsl.pum2.shoppingapp.gui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Stack;

import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.model.ShoppingListItemData;

public class AddNewItemsFragment extends Fragment {

    private Stack<ShoppingListItemData> listItemDataStack;
    private NewItemsListAdapter newItemsListAdapter;
    private RecyclerView newItemsRecyclerView;
    private Activity activity;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listItemDataStack = new Stack<>();
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_new_item, container, false);
        activity = getActivity();
        newItemsRecyclerView = (RecyclerView)view.findViewById(R.id.new_items_list_recycler_view);
        newItemsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager newItemsListLayoutManager = new LinearLayoutManager(activity);
        newItemsRecyclerView.setLayoutManager(newItemsListLayoutManager);
        newItemsListAdapter = new NewItemsListAdapter(listItemDataStack);
        newItemsRecyclerView.setAdapter(newItemsListAdapter);
        if (listItemDataStack.size() == 0) {
            insertNewItem();
        }
        setupButtons();
        return view;
    }

    private void insertNewItem() {
        listItemDataStack.push(new ShoppingListItemData());
        newItemsListAdapter.notifyItemInserted(0);
        newItemsRecyclerView.scrollToPosition(0);
    }

    private void setupButtons() {
        Button addNewItemButton = (Button)view.findViewById(R.id.add_new_item_button);
        addNewItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNewItem();
            }
        });
        Button doneButton = (Button)view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: wstawienie danych do bazy
                activity.finish();
            }
        });

    }

}
