package pl.polsl.pum2.shoppingapp.gui;

import android.support.v4.app.DialogFragment;

import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.Product;

public class ProductsFragment extends BaseRealmRecyclerViewFragment<Product> implements DeleteItemDialogFragment.DeleteItemDialogListener {

    int positionOfItemToDelete;

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
        DialogFragment deleteItemDialogFragment = DeleteItemDialogFragment.newInstance(getString(R.string.delete_product_message), getListItems().get(position).getName(), this);
        deleteItemDialogFragment.show(getActivity().getSupportFragmentManager(), "deleteItemDialogTag");
    }

    @Override
    public void onDeleteItemDialogOK() {
        removeItem(positionOfItemToDelete);
    }

    @Override
    public void onDeleteItemDialogCancel() {

    }

}
