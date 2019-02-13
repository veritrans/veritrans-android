package com.midtrans.sdk.uikit.base.callback;

import java.io.Serializable;

public interface PaymentResult<T> extends Serializable {

    void onPaymentFinished(final Result result, final T response);

    void onFailed(Throwable throwable);
}