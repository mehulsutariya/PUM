package pl.polsl.pum2.shoppingapp.gui;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.text.NumberFormat;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.ShoppingList;
import pl.polsl.pum2.shoppingapp.database.ShoppingListItem;

public class ShoppingListFragment extends Fragment {

    public final static int SHOPPING_LIST = 0;
    public final static int CART = 1;
    private static final String LIST_NAME = "listName";
    private static final String LIST_TYPE = "listType";
    private String listName;
    private OnFragmentInteractionListener listener;
    private RealmRecyclerView shoppingListRecyclerView;
    private ShoppingListAdapter shoppingListAdapter;
    private ShoppingList shoppingList;
    private int positionOfItemToDelete;
    private int numberOfPositionsInEditMode;
    private Activity activity;
    private int listType;
    private Realm realm;

    private RealmResults<ShoppingListItem> listItems;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    public static ShoppingListFragment newInstance(String listName, int listType) {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle arguments = new Bundle();
        arguments.putString(LIST_NAME, listName);
        arguments.putInt(LIST_TYPE, listType);
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
        listType = getArguments().getInt(LIST_TYPE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        shoppingListRecyclerView = (RealmRecyclerView) view.findViewById(R.id.shopping_list_recycler_view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
            shoppingList = realm.where(ShoppingList.class).equalTo("name", listName).findFirst();

            if (listType == SHOPPING_LIST) {
                listItems = shoppingList.getItems().where().equalTo("isBought", false).findAll();
            } else {
                listItems = shoppingList.getItems().where().equalTo("isBought", true).findAll();
            }
            updatePriceSum();

        setRecyclerViewAdapter();
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void setRecyclerViewAdapter() {
        shoppingListAdapter = new ShoppingListAdapter(getContext(), listItems, true, false, listType);
        shoppingListAdapter.setOnItemClickListener(new ShoppingListAdapter.OnItemClickListener() {

            @Override
            public void onDeleteButton(int position) {
                DialogFragment deleteItemDialogFragment = DeleteItemDialogFragment.newInstance(shoppingListAdapter.getItemName(position));
                deleteItemDialogFragment.setTargetFragment(ShoppingListFragment.this, 1);
                deleteItemDialogFragment.show(getActivity().getSupportFragmentManager(), "deleteItemDialogTag");
                positionOfItemToDelete = position;
            }

            @Override
            public void onEditButton(int position) {
                enterEditMode();
            }

            @Override
            public void onDoneButton(int position, int editResult) {
                if (editResult == ShoppingListAdapter.EDIT_SUCCEEDED) {
                    exitEditMode();
                    updatePriceSum();
                } else {
                    MessageDialogFragment dialogFragment = MessageDialogFragment.newInstance(getString(R.string.valuesInvalid));
                    dialogFragment.show(getFragmentManager(), "itemErrorEditDialog");
                }
            }

            @Override
            public void onCancelButton(int position) {
                exitEditMode();
            }

            @Override
            public void onBuyButton(final int position, final int listType) {
                realm.beginTransaction();
                ShoppingListItem item = listItems.get(position);
                if (listType == SHOPPING_LIST) {
                    item.setBought(true);
                } else {
                    item.setBought(false);
                }
                if (listType == SHOPPING_LIST) {
                    listener.onItemBought();
                } else {
                    listener.onItemRestoredToList();
                }
                realm.commitTransaction();
                updatePriceSum();
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

        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    void removeItem() {
        realm.beginTransaction();
        RealmList<ShoppingListItem> listItems = shoppingList.getItems();
        ShoppingListItem itemToDelete = listItems.get(positionOfItemToDelete);
        itemToDelete.removeFromRealm();
        realm.commitTransaction();
        shoppingListAdapter.notifyItemRemoved(positionOfItemToDelete);
        listener.onItemRemoved();
        updatePriceSum();
    }

    void cancelItemRemoving() {
        listener.onItemRemovingCanceled();
    }

    private void updatePriceSum() {
        new PricesSumCounter().execute();
    }

    public interface OnFragmentInteractionListener {
        void onEnterEditMode();

        void onExitEditMode();

        void onItemRemoved();

        void onItemRemovingCanceled();

        void onItemBought();

        void onItemRestoredToList();

        void onBoughtItemsPriceSumChanged(String priceSum);
    }


    private class PricesSumCounter extends AsyncTask<Void, Void, Double> {
        protected Double doInBackground(Void... params) {
            Realm realm = Realm.getDefaultInstance();
            ShoppingList shoppingList = realm.where(ShoppingList.class).equalTo("name", listName).findFirst();
            RealmResults<ShoppingListItem> itemsWithPrice = shoppingList.getItems().where().greaterThan("price", 0.0).equalTo("isBought", true).findAll();
            double priceSum = 0;
            for (ShoppingListItem item : itemsWithPrice) {
                priceSum += item.getPrice() * item.getQuantity();
            }
            realm.close();
            return priceSum;
        }

        protected void onPostExecute(Double result) {
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            String priceSumStr = numberFormat.format(result);
            listener.onBoughtItemsPriceSumChanged(priceSumStr);
        }

    }

}
