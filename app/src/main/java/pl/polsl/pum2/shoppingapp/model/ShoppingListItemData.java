package pl.polsl.pum2.shoppingapp.model;

public class ShoppingListItemData {
    private String productName;
    private String productCategory;
    private Double priceValue;
    private Integer quantity;
    private boolean isBought;
    private boolean empty;

    public ShoppingListItemData(String productName, String productCategory, double priceValue, int quantity, boolean isBought) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.priceValue = priceValue;
        this.quantity = quantity;
        this.isBought = isBought;
        empty = false;
    }

    public ShoppingListItemData() {
        this("", "", 0.0, 0, false);
        empty = true;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
        setEmptyState();
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
        setEmptyState();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        setEmptyState();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getPriceString() {
        return priceValue.toString();
    }

    public double getPrice() {
        return priceValue;
    }

    public void setPrice(double priceValue) {
        this.priceValue = priceValue;
        setEmptyState();
    }

    public boolean getIsBought() {
        return isBought;
    }

    public void setIsBought(boolean isBought) {
        this.isBought = isBought;
    }

    public boolean isEmpty() {
        return empty;
    }

    private void setEmptyState() {
        //TODO: uwzględnić kategorię
        if (hasProductName() && quantityNotEqualsZero()) {
            empty = false;
        }
    }

    private boolean hasProductName() {
        return productName.length() != 0;
    }

    private boolean quantityNotEqualsZero() {
        return quantity != 0;
    }
}
