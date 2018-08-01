package com.midtrans.sdk.corekit.core;

/**
 * Created by ziahaqi on 8/1/18.
 */

public class PaymentException extends RuntimeException {

    public String statusCode;

    public PaymentException(Throwable cause, String statusCode, String errorMessage) {
        super(cause);
        this.statusCode = statusCode;
    }
}
