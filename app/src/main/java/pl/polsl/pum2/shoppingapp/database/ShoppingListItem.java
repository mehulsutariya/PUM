package pl.polsl.pum2.shoppingapp.database;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class ShoppingListItem extends RealmObject {
    private Product product;
    private int quantity;
    private double price;
    private boolean isBought;

    public ShoppingListItem() {
        quantity = 1;
        price = 0;
        isBought = false;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
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
}


