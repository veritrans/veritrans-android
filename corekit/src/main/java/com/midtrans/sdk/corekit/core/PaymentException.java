package com.midtrans.sdk.corekit.core;

/**
 * Created by ziahaqi on 8/1/18.
 */

public class PaymentException extends RuntimeException {

    public String statusCode;

    public PaymentException(String statusCode, String errorMessage, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }
}
