package pl.polsl.pum2.shoppingapp.gui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.model.ShoppingListItemData;

public class ShoppingListFragment extends Fragment {

    private RecyclerView shoppingListRecyclerView;
    private ShoppingListAdapter shoppingListAdapter;
    private List<ShoppingListItemData> shoppingListItemDataArray;
    private int positionOfItemToDelete;
    private int numberOfPositionsInEditMode;
    private Activity activity;
    private int listType;

    public interface OnFragmentInteractionListener {
        void onEnterEditMode();

        void onExitEditMode();

        void onItemRemoved();

        void onItemRemovingCanceled();
    }

    OnFragmentInteractionListener listener;

    public ShoppingListFragment() {
        // Required empty public constructor
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        shoppingListItemDataArray = new ArrayList<>();
        listType = getArguments().getInt("ListType");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        setRecyclerView(view);
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    private void setRecyclerView(View view) {
        shoppingListRecyclerView = (RecyclerView) view.findViewById(R.id.shopping_list_recycler_view);
        shoppingListRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager shoppingListLayoutManager;
        shoppingListLayoutManager = new LinearLayoutManager(getActivity());
        shoppingListRecyclerView.setLayoutManager(shoppingListLayoutManager);

        setRecyclerViewAdapter();
    }


    private void setRecyclerViewAdapter() {
        shoppingListAdapter = new ShoppingListAdapter(shoppingListItemDataArray, getContext(), listType);
        shoppingListAdapter.setOnItemClickListener(new ShoppingListAdapter.OnItemClickListener() {

            @Override
            public void onDeleteButton(int position) {
                DialogFragment deleteItemDialogFragment = new DeleteItemDialogFragment();
                deleteItemDialogFragment.setTargetFragment(ShoppingListFragment.this, 1);
                Bundle arguments = new Bundle();
                arguments.putString("ProductName", shoppingListAdapter.getItemName(position));
                deleteItemDialogFragment.setArguments(arguments);
                deleteItemDialogFragment.show(getActivity().getSupportFragmentManager(), "deleteItemDialogTag");
                positionOfItemToDelete = position;
            }

            @Override
            public void onEditButton(int position) {
                enterEditMode();
            }

            @Override
            public void onDoneButton(int position) {
                exitEditMode();
            }

            @Override
            public void onCancelButton(int position) {
                exitEditMode();
            }

            @Override
            public void onBuyButton(int position) {
                //TODO tymczasowo usuwa pozycję:
                shoppingListItemDataArray.remove(position);
                shoppingListAdapter.notifyItemRemoved(position);
            }
        });
        shoppingListRecyclerView.setAdapter(shoppingListAdapter);
    }

    private void enterEditMode() {
        if (numberOfPositionsInEditMode == 0) {
            listener.onEnterEditMode();
        }
        numberOfPositionsInEditMode++;
    }

    private void exitEditMode() {
        hideSoftKeyboard();
        numberOfPositionsInEditMode--;
        if (numberOfPositionsInEditMode == 0) {
            listener.onExitEditMode();
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    void removeItem() {
        shoppingListItemDataArray.remove(positionOfItemToDelete);
        shoppingListAdapter.notifyItemRemoved(positionOfItemToDelete);
        listener.onItemRemoved();
    }

    void cancelItemRemoving() {
        listener.onItemRemovingCanceled();
    }

    public void addNewItem(ShoppingListItemData itemData) {
        shoppingListItemDataArray.add(itemData);
        shoppingListAdapter.notifyItemInserted(shoppingListItemDataArray.size() - 1);
    }

}
