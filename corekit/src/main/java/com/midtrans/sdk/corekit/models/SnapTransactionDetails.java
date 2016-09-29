package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rakawm on 7/19/16.
 */
public class SnapTransactionDetails {
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("gross_amount")
    private int grossAmount;

    public SnapTransactionDetails(String orderId, int grossAmount) {
        setOrderId(orderId);
        setGrossAmount(grossAmount);
    }

    public double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(int grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
