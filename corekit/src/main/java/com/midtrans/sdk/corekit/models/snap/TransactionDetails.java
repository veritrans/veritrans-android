package com.midtrans.sdk.corekit.models.snap;

/**
 * @author rakawm
 */
public class TransactionDetails {
    private String orderId;
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
