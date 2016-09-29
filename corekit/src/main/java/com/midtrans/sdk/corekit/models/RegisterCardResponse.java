package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by alfaridi on 1/27/16.
 */
public class RegisterCardResponse extends TransactionResponse implements Serializable {

    @SerializedName("user_id")
    private String userId;

    public RegisterCardResponse(String statusCode, String statusMessage, String transactionId, String orderId, String grossAmount, String paymentType, String transactionTime, String transactionStatus) {
        super(statusCode, statusMessage, transactionId, orderId, grossAmount, paymentType, transactionTime, transactionStatus);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
