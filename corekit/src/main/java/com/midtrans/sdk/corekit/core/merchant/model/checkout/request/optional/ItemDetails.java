package com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional;

import java.io.Serializable;

public class ItemDetails implements Serializable {

    private String id;
    private double price;
    private int quantity;
    private String name;

    public ItemDetails() {
    }

    /**
     * @param id       unique id of the item.
     * @param price    price of the item.
     * @param quantity number of items that is purchased.
     * @param name     name of the item.
     */
    public ItemDetails(String id,
                       double price,
                       int quantity,
                       String name) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}