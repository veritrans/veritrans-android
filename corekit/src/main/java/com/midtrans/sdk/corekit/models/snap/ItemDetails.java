package com.midtrans.sdk.corekit.models.snap;

/**
 * Created by rakawm on 7/18/16.
 */
public class ItemDetails {
    private String name;
    private String price;
    private int quantity;

    public ItemDetails() {
    }

    public ItemDetails(String name, String price, int quantity) {
        setName(name);
        setPrice(price);
        setQuantity(quantity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
