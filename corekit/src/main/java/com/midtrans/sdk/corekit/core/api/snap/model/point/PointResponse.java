package com.midtrans.sdk.corekit.core.api.snap.model.point;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PointResponse implements Serializable {

    @SerializedName("point_balance")
    private String balance;
    @SerializedName("point_balance_amount")
    private String balanceAmount;
    @SerializedName("point_balance_quantity")
    private String balanceQuantity;
    @SerializedName("status_code")
    private String statusCode;
    @SerializedName("status_message")
    private String statusMessage;
    @SerializedName("transaction_time")
    private String transactionTime;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getBalanceQuantity() {
        return balanceQuantity;
    }

    public void setBalanceQuantity(String balanceQuantity) {
        this.balanceQuantity = balanceQuantity;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }
}