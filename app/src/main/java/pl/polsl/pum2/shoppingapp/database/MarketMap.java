package pl.polsl.pum2.shoppingapp.database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class MarketMap extends RealmObject implements CheckableRealmObjectWithName {
    @PrimaryKey
    private String name;
    private RealmList<ProductCategory> productCategories;
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(RealmList<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }
}
