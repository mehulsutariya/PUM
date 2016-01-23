package pl.polsl.pum2.shoppingapp.gui;

import android.support.v4.app.DialogFragment;

import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.MarketMap;

public class MarketMapsFragment extends BaseRealmRecyclerViewFragment<MarketMap> implements DeleteItemDialogFragment.DeleteItemDialogListener {

    int positionOfItemToDelete;
    private boolean isRemovingCheckedItems;

    public MarketMapsFragment() {
        // Required empty public constructor
    }


    @Override
    protected RealmResults<MarketMap> runRealmQuery() {
        return getRealmInstance().where(MarketMap.class).findAllSorted("name");
    }

    @Override
    protected void onRecyclerViewItemDelete(int position) {
        positionOfItemToDelete = position;
        isRemovingCheckedItems = false;
        DialogFragment deleteItemDialogFragment = DeleteItemDialogFragment.newInstance(getString(R.string.delete_map_message), getString(R.string.delete_map_extended_message), getListItems().get(position).getName(), this);
        deleteItemDialogFragment.show(getActivity().getSupportFragmentManager(), "deleteItemDialogTag");
    }

    @Override
    protected void onRecyclerViewCheckedItemsDelete() {
        isRemovingCheckedItems = true;
        DialogFragment deleteItemDialogFragment = DeleteItemDialogFragment.newInstance(getString(R.string.delete_multiple_maps_message), getString(R.string.delete_multiple_maps_extended_message), null, this);
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
