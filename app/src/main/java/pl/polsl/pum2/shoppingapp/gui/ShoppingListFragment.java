package pl.polsl.pum2.shoppingapp.gui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import io.realm.RealmChangeListener;
import io.realm.RealmList;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.ShoppingList;
import pl.polsl.pum2.shoppingapp.database.ShoppingListItem;

public class ShoppingListFragment extends BaseRealmFragment {

    private static final String LIST_NAME = "listName";
    private OnFragmentInteractionListener listener;
    private RecyclerView shoppingListRecyclerView;
    private ShoppingListAdapter shoppingListAdapter;
    private ShoppingList shoppingList;
    private int positionOfItemToDelete;
    private int numberOfPositionsInEditMode;
    private Activity activity;
    private int listType;
    private String listName;
    private RealmList<ShoppingListItem> listItems;
    private RealmChangeListener realmChangeListener;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    public static ShoppingListFragment newInstance(String listName) {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle arguments = new Bundle();
        arguments.putString(LIST_NAME, listName);
        fragment.setArguments(arguments);
        return fragment;
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
        listName = getArguments().getString(LIST_NAME);
        shoppingList = realm.where(ShoppingList.class).equalTo("name", listName).findFirst();
        listItems = shoppingList.getItems();
        listType = getArguments().getInt("ListType");
        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                shoppingListAdapter.notifyDataSetChanged();
            }
        };
        shoppingList.addChangeListener(realmChangeListener);
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
        shoppingListAdapter = new ShoppingListAdapter(listItems, getContext(), listType);
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
                //TODO:

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
        realm.beginTransaction();
        RealmList<ShoppingListItem> listItems = shoppingList.getItems();
        ShoppingListItem itemToDelete = listItems.get(positionOfItemToDelete);
        itemToDelete.removeFromRealm();
        listItems.clear();
        realm.commitTransaction();
        shoppingListAdapter.notifyItemRemoved(positionOfItemToDelete);
        listener.onItemRemoved();
    }

    void cancelItemRemoving() {
        listener.onItemRemovingCanceled();
    }

    public interface OnFragmentInteractionListener {
        void onEnterEditMode();

        void onExitEditMode();

        void onItemRemoved();

        void onItemRemovingCanceled();
    }

}
