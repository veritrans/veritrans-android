package com.midtrans.sdk.uikit.base.model;

public class PaymentException extends RuntimeException {

    public String statusCode;

    public PaymentException(String statusCode, String errorMessage, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}