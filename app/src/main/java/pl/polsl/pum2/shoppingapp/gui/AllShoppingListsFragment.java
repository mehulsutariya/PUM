package pl.polsl.pum2.shoppingapp.gui;

import android.content.Intent;
import android.support.v4.app.DialogFragment;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.ShoppingList;

public class AllShoppingListsFragment extends BaseRealmRecyclerViewFragment<ShoppingList> implements DeleteItemDialogFragment.DeleteItemDialogListener {

    private RealmResults<ShoppingList> shoppingLists;
    private int positionOfItemToDelete;
    private boolean isRemovingCheckedItems;


    public AllShoppingListsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        shoppingLists = getListItems();
    }

    @Override
    protected void onRecyclerViewItemClick(int position) {
        showShoppingList(position);
    }

    @Override
    protected void onRecyclerViewItemDelete(int position) {
        positionOfItemToDelete = position;
        isRemovingCheckedItems = false;
        DialogFragment deleteItemDialogFragment = DeleteItemDialogFragment.newInstance(getString(R.string.delete_list_message), shoppingLists.get(position).getName(), this);
        deleteItemDialogFragment.show(getActivity().getSupportFragmentManager(), "deleteItemDialogTag");
    }

    @Override
    protected void onRecyclerViewCheckedItemsDelete() {
        isRemovingCheckedItems = true;
        DialogFragment deleteItemDialogFragment = DeleteItemDialogFragment.newInstance(getString(R.string.delete_multiple_lists_message), this);
        deleteItemDialogFragment.show(getActivity().getSupportFragmentManager(), "deleteItemDialogTag");
    }

    @Override
    protected RealmResults<ShoppingList> runRealmQuery() {
        return getRealmInstance().where(ShoppingList.class).findAll();
    }

    private void showShoppingList(int position) {
        ShoppingList shoppingList = shoppingLists.get(position);
        Intent intent = new Intent(getActivity(), ShoppingListActivity.class);
        intent.putExtra(ShoppingListActivity.LIST_NAME, shoppingList.getName());
        startActivity(intent);
    }

    @Override
    public void onDeleteItemDialogOK() {
        if (isRemovingCheckedItems) {
            removeCheckedItems();
        } else {
            removeItem(positionOfItemToDelete);
        }
    }

    @Override
    public void removeItem(final int position) {
        Realm realm = getRealmInstance();
        ShoppingList shoppingList = getListItems().get(position);
        realm.beginTransaction();
        shoppingList.getItems().clear();
        realm.commitTransaction();
        super.removeItem(position);
    }


    @Override
    public void removeCheckedItems() {
        Realm realm = getRealmInstance();
        RealmResults<ShoppingList> itemsToDelete = getListItems().where().equalTo("checked", true).findAll();
        realm.beginTransaction();
        ShoppingList currentItem;
        for (int i = 0; i < itemsToDelete.size(); i++) {
            currentItem = itemsToDelete.get(i);
            currentItem.getItems().clear();
        }
        realm.commitTransaction();
        super.removeCheckedItems();
    }


    @Override
    public void onDeleteItemDialogCancel() {

    }

}
