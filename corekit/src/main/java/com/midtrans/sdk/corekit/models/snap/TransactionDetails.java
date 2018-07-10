package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

/**
 * @author rakawm
 */
public class TransactionDetails {
    @SerializedName("order_id")
    private String orderId;

    @SerializedName("gross_amount")
    private double amount;

    private String currency;

    public TransactionDetails() {
    }

    public TransactionDetails(String orderId, Double amount) {
        setOrderId(orderId);
        setAmount(amount);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }
}
