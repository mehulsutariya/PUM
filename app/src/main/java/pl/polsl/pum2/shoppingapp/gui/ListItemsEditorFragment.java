package pl.polsl.pum2.shoppingapp.gui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.model.ShoppingListItemData;

public class ListItemsEditorFragment extends Fragment {

    private static final String LAYOUT_MANAGER_STATE = "layoutManagerState";
    private List<ShoppingListItemData> listItems;
    private ListItemsEditorAdapter listAdapter;
    private RecyclerView recyclerView;
    private Activity activity;
    private View view;
    private LinearLayoutManager recyclerViewLayoutManager;
    private static final String MESSAGE_DIALOG_TAG = "messageDialogTag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listItems = new ArrayList<>();
        setRetainInstance(true);
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
        listItems.add(new ShoppingListItemData());
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
                for (ShoppingListItemData item : listItems) {
                    if (item.isEmpty()) {
                        formIsNotCompleted = true;
                        break;
                    }
                }
                if (formIsNotCompleted) {
                    showMessageDialog();
                } else {
                    //TODO: wstawienie danych do bazy
                    activity.finish();
                }
            }
        });

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
