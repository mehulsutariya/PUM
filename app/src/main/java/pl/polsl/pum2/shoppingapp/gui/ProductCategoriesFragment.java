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
import pl.polsl.pum2.shoppingapp.database.ProductCategory;

public class ProductCategoriesFragment extends Fragment {

    private RealmResults<ProductCategory> categories;
    private RealmRecyclerView categoriesRecyclerView;
    private ProductCategoriesAdapter adapter;
    private Realm realm;

    public ProductCategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_activity_list, container, false);
        categoriesRecyclerView = (RealmRecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
        categories = realm.where(ProductCategory.class).findAll();
        setRecyclerViewAdapter();
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }

    private void setRecyclerViewAdapter() {
        adapter = new ProductCategoriesAdapter(getContext(), categories, true, true);
        categoriesRecyclerView.setAdapter(adapter);
    }

}
