package com.midtrans.sdk.corekit.models;

/**
 * model class created for payment methods recycler view. it contains image resource Id, name of
 * payment method etc.
 *
 * Created by shivam on 10/19/15.
 */
public class PaymentMethodsModel {

    private String name;
    private int imageId;
    private int paymentType;
    private boolean isSelected;
    private Integer priority;

    public PaymentMethodsModel(String name, int imageId, int paymentType, int priority) {
        this.paymentType = paymentType;
        this.imageId = imageId;
        this.name = name;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Integer getPriority() {
        return priority;
    }
}