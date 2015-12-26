package pl.polsl.pum2.shoppingapp.gui;


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

public abstract class BasicRealmRecyclerViewFragment<T extends RealmObject & RealmObjectWithName> extends Fragment {

    private Realm realm;
    private RealmResults<T> listItems;
    private RealmRecyclerView productsRecyclerView;
    private BaseRecyclerViewRealmAdapter<T> adapter;

    public BasicRealmRecyclerViewFragment() {
        // Required empty public constructor
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

    private void setRecyclerViewAdapter() {
        adapter = new BaseRecyclerViewRealmAdapter<T>(getContext(), listItems, true, true);
        productsRecyclerView.setAdapter(adapter);
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


}