package pl.polsl.pum2.shoppingapp.gui;

import android.content.Intent;
import android.support.v4.app.DialogFragment;

import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.ShoppingList;

public class AllShoppingListsFragment extends BaseRealmRecyclerViewFragment<ShoppingList> implements DeleteItemDialogFragment.DeleteItemDialogListener {

    private RealmResults<ShoppingList> shoppingLists;
    private int positionOfItemToDelete;


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
        DialogFragment deleteItemDialogFragment = DeleteItemDialogFragment.newInstance(getString(R.string.delete_list_message), shoppingLists.get(position).getName(), this);
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
        removeItem(positionOfItemToDelete);
    }

    @Override
    public void onDeleteItemDialogCancel() {

    }

}
