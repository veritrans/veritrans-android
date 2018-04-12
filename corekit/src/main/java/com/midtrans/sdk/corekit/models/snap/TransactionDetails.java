package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

/**
 * @author rakawm
 */
public class TransactionDetails {
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("gross_amount")
    private long amount;

    public TransactionDetails() {
    }

    public TransactionDetails(String orderId, long amount) {
        setOrderId(orderId);
        setAmount(amount);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
