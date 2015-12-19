package pl.polsl.pum2.shoppingapp.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.Product;

public class ProductsFragment extends Fragment {

    private RealmResults<Product> products;
    private RealmRecyclerView productsRecyclerView;
    private ProductsAdapter adapter;
    private Realm realm;

    public ProductsFragment() {
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
        products = realm.where(Product.class).findAllSorted("name");
        setRecyclerViewAdapter();
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }

    private void setRecyclerViewAdapter() {
        adapter = new ProductsAdapter(getContext(), products, true, true);
        productsRecyclerView.setAdapter(adapter);
    }

}
