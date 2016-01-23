package pl.polsl.pum2.shoppingapp.gui;

import android.support.v4.app.DialogFragment;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.ProductCategory;
import pl.polsl.pum2.shoppingapp.database.ShoppingList;
import pl.polsl.pum2.shoppingapp.database.ShoppingListItem;

public class ProductCategoriesFragment extends BaseRealmRecyclerViewFragment<ProductCategory> implements DeleteItemDialogFragment.DeleteItemDialogListener {

    int positionOfItemToDelete;
    private boolean isRemovingCheckedItems;

    public ProductCategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    protected RealmResults<ProductCategory> runRealmQuery() {
        return getRealmInstance().where(ProductCategory.class).findAllSorted("name");
    }

    @Override
    protected void onRecyclerViewItemDelete(int position) {
        positionOfItemToDelete = position;
        isRemovingCheckedItems = false;
        DialogFragment deleteItemDialogFragment = DeleteItemDialogFragment.newInstance(getString(R.string.delete_category_message), getString(R.string.delete_category_extended_message), getListItems().get(position).getName(), this);
        deleteItemDialogFragment.show(getActivity().getSupportFragmentManager(), "deleteItemDialogTag");
    }

    @Override
    protected void onRecyclerViewCheckedItemsDelete() {
        isRemovingCheckedItems = true;
        DialogFragment deleteItemDialogFragment = DeleteItemDialogFragment.newInstance(getString(R.string.delete_multiple_categories_message), getString(R.string.delete_multiple_categories_extended_message), null, this);
        deleteItemDialogFragment.show(getActivity().getSupportFragmentManager(), "deleteItemDialogTag");
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
    public void onDeleteItemDialogCancel() {

    }

    @Override
    public void removeCheckedItems() {
        //TODO
    }

    @Override
    public void removeItem(int position) {
        //TODO
    }

}
