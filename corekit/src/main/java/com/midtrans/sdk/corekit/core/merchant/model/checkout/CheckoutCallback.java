package com.midtrans.sdk.corekit.core.merchant.model.checkout;

import com.midtrans.sdk.corekit.base.callback.HttpRequestCallback;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.response.CheckoutResponse;

public interface CheckoutCallback extends HttpRequestCallback {

    void onSuccess(CheckoutResponse token);

    void onFailure(CheckoutResponse token, String reason);
}
