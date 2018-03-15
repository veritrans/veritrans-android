package com.midtrans.sdk.corekit.models.snap;

/**
 * Created by rakawm on 7/18/16.
 */
public class ItemDetails {
    private String id;
    private String name;
    private long price;
    private int quantity;

    public ItemDetails() {

    }

    public ItemDetails(String id, String name, long price, int quantity) {
        setId(id);
        setName(name);
        setPrice(price);
        setQuantity(quantity);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
