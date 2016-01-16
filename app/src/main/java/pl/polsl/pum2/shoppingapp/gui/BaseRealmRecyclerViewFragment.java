package pl.polsl.pum2.shoppingapp.gui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.CheckableRealmObjectWithName;

public abstract class BaseRealmRecyclerViewFragment<T extends RealmObject & CheckableRealmObjectWithName> extends Fragment {

    private final String NUMBER_OF_CHECKED_ITEMS = "numberOfCheckedItems";
    private Realm realm;
    private RealmResults<T> listItems;
    private RealmRecyclerView productsRecyclerView;
    private BaseRecyclerViewRealmAdapter<T> adapter;
    private OnFragmentInteractionListener listener;
    private ActionMode actionMode;
    private ActionMode.Callback actionModeCallback;
    private int numberOfCheckedItems;

    public BaseRealmRecyclerViewFragment() {
        // Required empty public constructor
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
        setupActionMode();
        if (savedInstanceState != null) {
            numberOfCheckedItems = savedInstanceState.getInt(NUMBER_OF_CHECKED_ITEMS);
        } else {
            numberOfCheckedItems = 0;
        }
        if (numberOfCheckedItems > 0) {
            actionMode = getActivity().startActionMode(actionModeCallback);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_activity_list, container, false);
        productsRecyclerView = (RealmRecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
        listItems = runRealmQuery();
        setRecyclerViewAdapter();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.close();
        realm.close();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outBundle) {
        super.onSaveInstanceState(outBundle);
        outBundle.putInt(NUMBER_OF_CHECKED_ITEMS, numberOfCheckedItems);
    }

    private void setRecyclerViewAdapter() {
        adapter = new BaseRecyclerViewRealmAdapter<>(getContext(), listItems, true, true);
        productsRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerViewRealmAdapter.OnItemClickListener() {
            @Override
            public void onListItemClick(int position) {
                onRecyclerViewItemClick(position);
            }

            @Override
            public boolean onListItemLongClick(int position) {
                onRecyclerViewItemLongClick(position);
                return false;
            }

            @Override
            public void onDelete(int position) {
                onRecyclerViewItemDelete(position);
            }

            public void onItemEdit() {
                listener.onEnterEditMode();
            }

            @Override
            public void onItemEditFailed(int position) {
                MessageDialogFragment dialogFragment = MessageDialogFragment.newInstance(getString(R.string.nameAlreadyExists));
                dialogFragment.show(getFragmentManager(), "nameExistsMessageDialog");
            }

            @Override
            public void onItemChecked(int position) {
                realm.beginTransaction();
                listItems.get(position).setChecked(true);
                realm.commitTransaction();
                numberOfCheckedItems++;
                if (actionMode == null) {
                    actionMode = getActivity().startActionMode(actionModeCallback);
                }
            }

            @Override
            public void onItemUnchecked(int position) {
                realm.beginTransaction();
                listItems.get(position).setChecked(false);
                realm.commitTransaction();
                numberOfCheckedItems--;
                if (numberOfCheckedItems == 0 && actionMode != null) {
                    actionMode.finish();
                }
            }
        });

    }

    protected void onRecyclerViewItemClick(int position) {

    }

    protected void onRecyclerViewItemLongClick(int position) {

    }


    protected void onRecyclerViewItemDelete(int position) {

    }

    protected void onRecyclerViewCheckedItemsDelete() {

    }

    protected RealmResults<T> getListItems() {
        return listItems;
    }

    protected BaseRecyclerViewRealmAdapter<T> getAdapter() {
        return adapter;
    }

    protected Realm getRealmInstance() {
        return realm;
    }

    protected abstract RealmResults<T> runRealmQuery();

    public void removeItem(final int position) {
        realm.beginTransaction();
        listItems.remove(position);
        realm.commitTransaction();
    }

    public void removeCheckedItems() {
        RealmResults<T> itemsToDelete = listItems.where().equalTo("checked", true).findAll();
        realm.beginTransaction();
        itemsToDelete.clear();
        realm.commitTransaction();
        if (actionMode != null) {
            actionMode.finish();
            actionMode = null;
        }
    }

    private void setupActionMode() {
        actionModeCallback = new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate a menu resource providing context menu items
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_contextual_action_mode, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false; // Return false if nothing is done
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        onRecyclerViewCheckedItemsDelete();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
                RealmResults<T> checkedItems = listItems.where().equalTo("checked", true).findAll();
                if (checkedItems.size() > 0) {
                    realm.beginTransaction();
                    for (int i = checkedItems.size() - 1; i >=0; i--) {
                        checkedItems.get(i).setChecked(false);
                    }
                    realm.commitTransaction();
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }

    public interface OnFragmentInteractionListener {
        void onEnterEditMode();

        void onExitEditMode();
    }

}
