package com.midtrans.sdk.uikit.base.callback;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.uikit.base.enums.PaymentStatus;

public class Result {
    @PaymentStatus
    private String paymentStatus;
    private String paymentMessage;
    @PaymentType
    private String paymentType;

    public Result() {
    }

    public Result(@PaymentStatus String paymentStatus, String paymentMessage, @PaymentType String paymentType) {
        this.paymentStatus = paymentStatus;
        this.paymentMessage = paymentMessage;
        this.paymentType = paymentType;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(@PaymentStatus String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMessage() {
        return paymentMessage;
    }

    public void setPaymentMessage(String paymentMessage) {
        this.paymentMessage = paymentMessage;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(@PaymentType String paymentType) {
        this.paymentType = paymentType;
    }
}