package com.midtrans.sdk.ui.models;

/**
 * model class created for payment methods recycler view. it contains image resource Id, name of
 * payment method etc.
 *
 * Created by ziahaqi on 2/19/17.
 */
public class PaymentMethodModel {

    private String name;
    private int imageId;
    private int paymentType;
    private String description;
    private boolean isSelected;
    private Integer priority;

    public PaymentMethodModel(String name, String description, int imageId, int priority) {
        this.paymentType = paymentType;
        this.imageId = imageId;
        this.name = name;
        this.description = description;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public Integer getPriority() {
        return priority;
    }
}