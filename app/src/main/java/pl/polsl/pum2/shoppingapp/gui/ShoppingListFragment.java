package pl.polsl.pum2.shoppingapp.gui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.NumberFormat;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.MarketMap;
import pl.polsl.pum2.shoppingapp.database.ProductCategory;
import pl.polsl.pum2.shoppingapp.database.ShoppingList;
import pl.polsl.pum2.shoppingapp.database.ShoppingListItem;
import pl.polsl.pum2.shoppingapp.helpers.KeyboardHelper;

public class ShoppingListFragment extends Fragment implements DeleteItemDialogFragment.DeleteItemDialogListener {

    public final static int SHOPPING_LIST = 0;
    public final static int CART = 1;
    private static final String LIST_NAME = "listName";
    private static final String LIST_TYPE = "listType";
    private String listName;
    private OnFragmentInteractionListener listener;
    private RealmRecyclerView shoppingListRecyclerView;
    private ShoppingListAdapter shoppingListAdapter;
    private int positionOfItemToDelete;
    private int numberOfPositionsInEditMode;
    private int listType;
    private Realm realm;

    private RealmResults<ShoppingListItem> listItems;
    private RealmList<ProductCategory> productCategories;

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
        ShoppingList shoppingList = realm.where(ShoppingList.class).equalTo("name", listName).findFirst();
        MarketMap map = shoppingList.getMarketMap();
        if (map != null) {
            productCategories = map.getProductCategories();
            realm.beginTransaction();
            for (ShoppingListItem item : shoppingList.getItems()) {
                ProductCategory category = item.getCategory();
                if (category != null) {
                    item.setCategoryIndex(productCategories.indexOf(item.getCategory()));
                } else {
                    item.setCategoryIndex(-1);
                }
            }
            realm.commitTransaction();
            if (listType == SHOPPING_LIST) {
                listItems = shoppingList.getItems().where().equalTo("isBought", false).isNotNull("product").findAllSorted("categoryIndex");
            } else {
                listItems = shoppingList.getItems().where().equalTo("isBought", true).isNotNull("product").findAllSorted("categoryIndex");
            }
        } else {
            if (listType == SHOPPING_LIST) {
                listItems = shoppingList.getItems().where().equalTo("isBought", false).isNotNull("product").findAll();
            } else {
                listItems = shoppingList.getItems().where().equalTo("isBought", true).isNotNull("product").findAll();
            }
            productCategories = null;
        }

        updatePriceSum();
        setRecyclerViewAdapter();
    }

    @Override
    public void onStop() {
        super.onStop();
        shoppingListAdapter.close();
        realm.close();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void setRecyclerViewAdapter() {
        shoppingListAdapter = new ShoppingListAdapter(getContext(), realm, listItems, productCategories, true, true, listType);
        shoppingListAdapter.setOnItemClickListener(new ShoppingListAdapter.OnItemClickListener() {

            @Override
            public void onDeleteButton(int position) {
                DialogFragment deleteItemDialogFragment = DeleteItemDialogFragment.newInstance(getString(R.string.delete_product_message), shoppingListAdapter.getItemName(position), ShoppingListFragment.this);
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
                if (position >= 0) {
                    realm.beginTransaction();
                    ShoppingListItem item = listItems.get(position);
                    if (listType == SHOPPING_LIST) {
                        item.setBought(true);
                    } else {
                        item.setBought(false);
                    }
                    realm.commitTransaction();
                    if (listType == SHOPPING_LIST) {
                        listener.onItemBought();
                    } else {
                        listener.onItemRestoredToList();
                    }
                    updatePriceSum();
                }
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
        KeyboardHelper.hideSoftKeyboard(getActivity());
        numberOfPositionsInEditMode--;
        if (numberOfPositionsInEditMode == 0) {
            listener.onExitEditMode();
        }
    }

    @Override
    public void onDeleteItemDialogOK() {
        realm.beginTransaction();
        ShoppingListItem itemToDelete = listItems.get(positionOfItemToDelete);
        itemToDelete.removeFromRealm();
        realm.commitTransaction();
        listener.onItemRemoved();
        updatePriceSum();
    }

    @Override
    public void onDeleteItemDialogCancel() {
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
