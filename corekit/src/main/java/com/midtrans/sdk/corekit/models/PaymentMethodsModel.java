package com.midtrans.sdk.corekit.models;

/**
 * model class created for payment methods recycler view. it contains image resource Id, name of
 * payment method etc.
 * <p>
 * Created by shivam on 10/19/15.
 */
public class PaymentMethodsModel {

    private String status;
    private String name;
    private int imageId;
    private String paymentType;
    private String description;
    private boolean isSelected;
    private Integer priority;
    private boolean havePromo;

    public PaymentMethodsModel(String name, String description, int imageId, String paymentType, int priority, String status) {
        this.paymentType = paymentType;
        this.imageId = imageId;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.status = status;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public boolean isHavePromo() {
        return havePromo;
    }

    public void setHavePromo(boolean havePromo) {
        this.havePromo = havePromo;
    }
}