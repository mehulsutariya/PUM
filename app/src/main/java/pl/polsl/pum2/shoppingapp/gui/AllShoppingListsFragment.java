package pl.polsl.pum2.shoppingapp.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.ShoppingList;

public class AllShoppingListsFragment extends Fragment {

    private RealmResults<ShoppingList> shoppingLists;
    private RealmRecyclerView shoppingListsRecyclerView;
    private AllShoppingListsAdapter adapter;
    private Realm realm;

    public AllShoppingListsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_activity_list, container, false);
        shoppingListsRecyclerView = (RealmRecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
        shoppingLists = realm.where(ShoppingList.class).findAll();
        setRecyclerViewAdapter();

    }

    public void onStop() {
        super.onStop();
        realm.close();
    }


    private void setRecyclerViewAdapter() {
        adapter = new AllShoppingListsAdapter(getContext(), shoppingLists, true, true);
        adapter.setOnItemClickListener(new AllShoppingListsAdapter.OnItemClickListener() {
            @Override
            public void onListItemClick(int position) {
                showShoppingList(position);
            }
        });
        shoppingListsRecyclerView.setAdapter(adapter);
    }

    private void showShoppingList(int position) {
        ShoppingList shoppingList = shoppingLists.get(position);
        Intent intent = new Intent(getActivity(), ShoppingListActivity.class);
        intent.putExtra(ShoppingListActivity.LIST_NAME, shoppingList.getName());
        startActivity(intent);
    }

}
