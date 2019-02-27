package com.midtrans.sdk.uikit.base.callback;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.uikit.base.enums.PaymentStatus;

public class Result {
    @PaymentStatus
    private String paymentStatus;
    @PaymentType
    private String paymentType;

    public Result() {
    }

    public Result(
            @PaymentStatus String paymentStatus,
            @PaymentType String paymentType) {
        this.paymentStatus = paymentStatus;
        this.paymentType = paymentType;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(@PaymentStatus String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(@PaymentType String paymentType) {
        this.paymentType = paymentType;
    }
}