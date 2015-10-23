package pl.polsl.pum2.shoppingapp.model;

public class ShoppingListItemData {
    private String productName;
    private String productCategory;
    private Double priceValue;
    private Integer quantity;
    private boolean isBought;

    public ShoppingListItemData(String productName, String productCategory, double priceValue, int quantity,boolean isBought) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.priceValue = priceValue;
        this.quantity = quantity;
        this.isBought = isBought;
    }

    public ShoppingListItemData(){
        this("", "", 0.0, 1, false);
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getQuantityString(){
        return quantity.toString();
    }

    public String getPriceString() {
        return priceValue.toString();
    }

    public void setPriceValue(double priceValue) {
        this.priceValue = priceValue;
    }

    public boolean getIsBought() {
        return isBought;
    }

    public void setIsBought(boolean isBought) {
        this.isBought = isBought;
    }
}
