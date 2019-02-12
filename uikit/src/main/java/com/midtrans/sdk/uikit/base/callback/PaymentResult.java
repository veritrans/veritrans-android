package com.midtrans.sdk.uikit.base.callback;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.uikit.base.enums.PaymentStatus;

public interface PaymentResult<T> {

    void onPaymentFinished(final @PaymentStatus String statusMessage, final @PaymentType String paymentType, final T response);

    void onFailed(Throwable throwable);
}