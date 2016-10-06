package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * model class to hold information returned by CancelTransaction api call. contains status message
 * and status code of cancel transaction api call. Created by shivam on 10/26/15.
 */
public class TransactionCancelResponse {


    /**
     * statusMessage : Merchant cannot modify the status of the transaction status_code : 412
     */
    @SerializedName("status_message")
    private String statusMessage;

    @SerializedName("status_code")
    private String statusCode;

    public void setStatus_code(String status_code) {
        this.statusCode = status_code;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
