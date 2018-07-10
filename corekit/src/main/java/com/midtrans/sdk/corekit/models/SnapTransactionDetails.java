package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rakawm on 7/19/16.
 */
public class SnapTransactionDetails {
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("gross_amount")
    private Double grossAmount;
    private String currency;

    public SnapTransactionDetails(String orderId, Double grossAmount) {
        setOrderId(orderId);
        setGrossAmount(grossAmount);
    }

    public Double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(Double grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
