package com.midtrans.sdk.ui.models;

import java.io.Serializable;

/**
 * model class created for payment methods recycler view. it contains image resource Id, name of
 * payment method etc.
 *
 * Created by ziahaqi on 2/19/17.
 */
public class PaymentMethodModel implements Serializable{

    private final String paymentType;
    private  String paymentCategory;
    private String name;
    private int imageId;
    private String description;

    public PaymentMethodModel(String name, String description, String paymentType, int imageId) {
        this.imageId = imageId;
        this.name = name;
        this.description = description;
        this.paymentType = paymentType;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public String getDescription() {
        return description;
    }

    public String getPaymentType() {
        return paymentType;
    }
}