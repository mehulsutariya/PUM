package pl.polsl.pum2.shoppingapp.gui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.Product;

public class ProductsFragment extends BaseRealmFragment {

    private RealmResults<Product> products;
    private RealmRecyclerView productsRecyclerView;
    private ProductsAdapter adapter;

    public ProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        products = realm.where(Product.class).findAll();
    }

    private void setupRecyclerView(View view) {
        productsRecyclerView = (RealmRecyclerView) view.findViewById(R.id.recycler_view);
        setRecyclerViewAdapter();
    }

    private void setRecyclerViewAdapter() {
        adapter = new ProductsAdapter(getContext(), products, true, true);
        productsRecyclerView.setAdapter(adapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_activity_list, container, false);
        setupRecyclerView(view);
        return view;
    }


}
