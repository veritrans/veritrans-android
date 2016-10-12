package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

/**
 * @author rakawm
 */
public class TransactionDetails {
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("gross_amount")
    private int amount;

    public TransactionDetails() {
    }

    public TransactionDetails(String orderId, int amount) {
        setOrderId(orderId);
        setAmount(amount);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
