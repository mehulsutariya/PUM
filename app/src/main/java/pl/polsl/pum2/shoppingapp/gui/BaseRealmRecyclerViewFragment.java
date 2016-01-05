package pl.polsl.pum2.shoppingapp.gui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.RealmObjectWithName;

public abstract class BaseRealmRecyclerViewFragment<T extends RealmObject & RealmObjectWithName> extends Fragment {

    private Realm realm;
    private RealmResults<T> listItems;
    private RealmRecyclerView productsRecyclerView;
    private BaseRecyclerViewRealmAdapter<T> adapter;
    private OnFragmentInteractionListener listener;

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
        realm.close();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
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
        });

    }

    protected void onRecyclerViewItemClick(int position) {

    }

    protected void onRecyclerViewItemLongClick(int position) {

    }


    protected void onRecyclerViewItemDelete(int position) {

    }

    protected BaseRecyclerViewRealmAdapter<T> getAdapter() {
        return adapter;
    }

    protected RealmResults<T> getListItems() {
        return listItems;
    }

    protected Realm getRealmInstance() {
        return realm;
    }

    protected abstract RealmResults<T> runRealmQuery();

    public void removeItem(final int position) {
        realm.removeAllChangeListeners(); //obejście błędu biblioteki
        realm.beginTransaction();
        listItems.remove(position);
        realm.commitTransaction();
        //powiadomienia adaptera o zmianach bo listener został usunięty:
        adapter.notifyItemRemoved(position);
    }

    public interface OnFragmentInteractionListener {
        void onEnterEditMode();

        void onExitEditMode();
    }

}
