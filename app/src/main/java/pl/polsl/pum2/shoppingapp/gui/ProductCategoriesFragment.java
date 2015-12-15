package pl.polsl.pum2.shoppingapp.gui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.ProductCategory;

public class ProductCategoriesFragment extends BaseRealmFragment {

    private RealmResults<ProductCategory> categories;
    private RealmRecyclerView categoriesRecyclerView;
    private ProductCategoriesAdapter adapter;

    public ProductCategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categories = realm.where(ProductCategory.class).findAll();
    }

    private void setupRecyclerView(View view) {
        categoriesRecyclerView = (RealmRecyclerView) view.findViewById(R.id.recycler_view);
        setRecyclerViewAdapter();
    }

    private void setRecyclerViewAdapter() {
        adapter = new ProductCategoriesAdapter(getContext(), categories, true, true);
        categoriesRecyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_activity_list, container, false);
        setupRecyclerView(view);
        return view;
    }


}
