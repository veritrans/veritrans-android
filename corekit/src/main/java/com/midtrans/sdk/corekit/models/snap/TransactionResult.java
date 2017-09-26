package com.midtrans.sdk.corekit.models.snap;

import com.midtrans.sdk.corekit.models.TransactionResponse;

/**
 * Created by ziahaqi on 9/1/16.
 */
public class TransactionResult {
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_INVALID = "invalid";
    public static final String STATUS_FAILED = "failed";

    private boolean transactionCanceled;
    private TransactionResponse response;
    private String source;
    private String status;
    private String statusMessage;

    public TransactionResult(TransactionResponse response) {
        setResponse(response);
    }

    public TransactionResult() {

    }

    public TransactionResult(TransactionResponse response, String source, String status) {
        setResponse(response);
        setSource(source);
        setStatus(status);
    }

    public TransactionResult(boolean transactionCanceled) {
        this.transactionCanceled = transactionCanceled;
    }

    public TransactionResult(String paymentStatus, String statusMessage) {
        this.status = paymentStatus;
        this.statusMessage = statusMessage;
    }

    public TransactionResponse getResponse() {
        return response;
    }

    public void setResponse(TransactionResponse response) {
        this.response = response;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isTransactionCanceled() {
        return transactionCanceled;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
