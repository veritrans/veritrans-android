package com.midtrans.sdk.uikit.base.callback;

import com.midtrans.sdk.uikit.base.model.PaymentResponse;

import java.io.Serializable;

public interface PaymentResult extends Serializable {

    void onPaymentFinished(final Result result, PaymentResponse response);

    void onFailed(Throwable throwable);
}