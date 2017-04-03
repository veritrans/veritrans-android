package com.midtrans.sdk.ui.models;

import android.text.TextUtils;

import com.midtrans.sdk.core.models.snap.BaseTransactionResponse;
import com.midtrans.sdk.ui.constants.PaymentStatus;

import java.io.Serializable;

/**
 * Created by ziahaqi on 2/20/17.
 */

public class PaymentResult<T extends BaseTransactionResponse> implements Serializable {
    private boolean canceled;
    private String errorMessage;
    private String paymentStatus;
    private T transactionResponse;

    public PaymentResult(T paymentResponse) {
        this.transactionResponse = paymentResponse;
        initPaymentStatus();
    }

    public PaymentResult(boolean canceled) {
        this.canceled = canceled;
    }

    public PaymentResult(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private void initPaymentStatus() {
        if (transactionResponse.statusCode.equals(PaymentStatus.CODE_200) ||
                transactionResponse.transactionStatus.equalsIgnoreCase(PaymentStatus.SUCCESS) ||
                transactionResponse.transactionStatus.equalsIgnoreCase(PaymentStatus.SETTLEMENT)) {
            paymentStatus = PaymentStatus.SUCCESS;
        } else if (transactionResponse.statusCode.equals(PaymentStatus.PENDING) || transactionResponse.transactionStatus.equalsIgnoreCase(PaymentStatus.PENDING)) {
            if (transactionResponse.fraudStatus.equalsIgnoreCase(PaymentStatus.CHALLENGE)) {
                paymentStatus = PaymentStatus.CHALLENGE;
            } else {
                paymentStatus = PaymentStatus.PENDING;
            }
        } else if (TextUtils.isEmpty(errorMessage)) {
            this.paymentStatus = PaymentStatus.INVALID;
        } else {
            this.paymentStatus = PaymentStatus.FAILED;
        }
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public T getTransactionResponse() {
        return transactionResponse;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
