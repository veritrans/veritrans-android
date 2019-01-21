package com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Item implements Serializable {

    private String id;
    private double price;
    private int quantity;
    private String name;
    private String brand;
    private String category;
    @SerializedName("merchant_name")
    private String merchantName;

    public Item() {
    }

    /**
     * @param id       unique id of the item.
     * @param price    price of the item.
     * @param quantity number of items that is purchased.
     * @param name     name of the item.
     */
    public Item(
            String id,
            double price,
            int quantity,
            String name) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.name = name;
    }

    public Item(
            String id,
            double price,
            int quantity,
            String name,
            String brand,
            String category,
            String merchantName) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.merchantName = merchantName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
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

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}