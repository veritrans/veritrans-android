package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shivam on 11/30/15.
 */
public class BBMMoneyRequestModel {


    @SerializedName("payment_type")
    private String paymentType;

    @SerializedName("transaction_details")
    private TransactionDetails transactionDetails;

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }
}
