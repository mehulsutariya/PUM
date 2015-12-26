package pl.polsl.pum2.shoppingapp.gui;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.database.ShoppingList;

public class AllShoppingListsFragment extends BasicRealmRecyclerViewFragment<ShoppingList> {

    ActionMode actionMode;
    ActionMode.Callback mActionModeCallback;
    private RealmResults<ShoppingList> shoppingLists;


    public AllShoppingListsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionMode();
    }

    @Override
    public void onStart() {
        super.onStart();
        shoppingLists = getListItems();
        setRecyclerViewAdapter();

    }

    @Override
    protected RealmResults<ShoppingList> runRealmQuery() {
        return getRealmInstance().where(ShoppingList.class).findAll();
    }

    private void setRecyclerViewAdapter() {
        BaseRecyclerViewRealmAdapter<ShoppingList> adapter = getAdapter();
        adapter.setOnItemClickListener(new BaseRecyclerViewRealmAdapter.OnItemClickListener() {
            @Override
            public void onListItemClick(int position) {
                showShoppingList(position);
            }

            @Override
            public boolean onListItemLongClick(int position) {
                if (actionMode != null) {
                    return false;
                }
                actionMode = getActivity().startActionMode(mActionModeCallback);
                return true;
            }
        });
    }


    private void showShoppingList(int position) {
        ShoppingList shoppingList = shoppingLists.get(position);
        Intent intent = new Intent(getActivity(), ShoppingListActivity.class);
        intent.putExtra(ShoppingListActivity.LIST_NAME, shoppingList.getName());
        startActivity(intent);
    }

    private void setupActionMode() {
        mActionModeCallback = new ActionMode.Callback() {

            // Called when the action mode is created; startActionMode() was called
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate a menu resource providing context menu items
                //MenuInflater inflater = mode.getMenuInflater();
                //inflater.inflate(R.menu.context_menu, menu);
                return true;
            }

            // Called each time the action mode is shown. Always called after onCreateActionMode, but
            // may be called multiple times if the mode is invalidated.
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false; // Return false if nothing is done
            }

            // Called when the user selects a contextual menu item
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                /*
                switch (item.getItemId()) {
                    case R.id.menu_share:
                        shareCurrentItem();
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    default:
                        return false;
                }
                */
                return false;
            }

            // Called when the user exits the action mode
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
            }
        };
    }

}
