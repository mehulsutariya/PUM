package pl.polsl.pum2.shoppingapp.gui;

import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.database.ProductCategory;

public class ProductCategoriesFragment extends BaseRealmRecyclerViewFragment<ProductCategory> {

    public ProductCategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    protected RealmResults<ProductCategory> runRealmQuery() {
        return getRealmInstance().where(ProductCategory.class).findAllSorted("name");
    }

}
