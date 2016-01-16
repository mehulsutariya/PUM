package pl.polsl.pum2.shoppingapp.gui;

import android.support.v4.app.DialogFragment;

import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.Product;

public class ProductsFragment extends BaseRealmRecyclerViewFragment<Product> implements DeleteItemDialogFragment.DeleteItemDialogListener {

    int positionOfItemToDelete;
    private boolean isRemovingCheckedItems;

    public ProductsFragment() {
        // Required empty public constructor
    }


    @Override
    protected RealmResults<Product> runRealmQuery() {
        return getRealmInstance().where(Product.class).findAllSorted("name");
    }

    @Override
    protected void onRecyclerViewItemDelete(int position) {
        positionOfItemToDelete = position;
        isRemovingCheckedItems = false;
        DialogFragment deleteItemDialogFragment = DeleteItemDialogFragment.newInstance(getString(R.string.delete_product_message), getString(R.string.delete_product_extended_message), getListItems().get(position).getName(), this);
        deleteItemDialogFragment.show(getActivity().getSupportFragmentManager(), "deleteItemDialogTag");
    }

    @Override
    protected void onRecyclerViewCheckedItemsDelete() {
        isRemovingCheckedItems = true;
        DialogFragment deleteItemDialogFragment = DeleteItemDialogFragment.newInstance(getString(R.string.delete_multiple_products_message), getString(R.string.delete_multiple_products_extended_message), null, this);
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

}
