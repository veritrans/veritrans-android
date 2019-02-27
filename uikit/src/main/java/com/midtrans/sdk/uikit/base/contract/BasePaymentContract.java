package com.midtrans.sdk.uikit.base.contract;

public interface BasePaymentContract extends BaseContract {

    <T> void onPaymentSuccess(T response);

    void onPaymentError(Throwable error);

}