package pl.polsl.pum2.shoppingapp.database;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class ShoppingListItem extends RealmObject {
    @PrimaryKey
    private long timestamp;
    private Product product;
    private double quantity;
    private double price;
    private boolean isBought;
    private ProductCategory category;
    private int categoryIndex;

    public ShoppingListItem() {
        timestamp = System.currentTimeMillis();
        quantity = 1;
        price = 0;
        isBought = false;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean isBought) {
        this.isBought = isBought;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public int getCategoryIndex() {
        return categoryIndex;
    }

    public void setCategoryIndex(int categoryIndex) {
        this.categoryIndex = categoryIndex;
    }
}


