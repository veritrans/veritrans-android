package com.midtrans.sdk.uikit.models;

public class UobPayment {

    private String type;
    private String paymentName;
    private int image;
    private Integer priority = 0;
    private String description;
    private String status;

    public UobPayment(String type, String paymentName, int image, Integer priority, String description, String status) {
        this.type = type;
        this.paymentName = paymentName;
        this.image = image;
        this.priority = priority;
        this.description = description;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public int getImage() {
        return image;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
}
