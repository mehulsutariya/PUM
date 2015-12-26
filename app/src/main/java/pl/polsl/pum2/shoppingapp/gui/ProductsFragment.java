package pl.polsl.pum2.shoppingapp.gui;

import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.database.Product;

public class ProductsFragment extends BasicRealmRecyclerViewFragment<Product> {


    public ProductsFragment() {
        // Required empty public constructor
    }


    @Override
    protected RealmResults<Product> runRealmQuery() {
        return getRealmInstance().where(Product.class).findAllSorted("name");
    }

}
