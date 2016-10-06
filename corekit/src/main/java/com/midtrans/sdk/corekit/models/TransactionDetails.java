package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * holds information about amount and order Id
 *
 * Created by shivam on 10/29/15.
 */
public class TransactionDetails {
    /**
     * grossAmount : 100 orderId : 10938011
     */

    @SerializedName("gross_amount")
    private String grossAmount;

    @SerializedName("order_id")
    private String orderId;

    public TransactionDetails() {

    }

    public TransactionDetails(String grossAmount, String orderId) {
        this.grossAmount = grossAmount;
        this.orderId = orderId;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}
