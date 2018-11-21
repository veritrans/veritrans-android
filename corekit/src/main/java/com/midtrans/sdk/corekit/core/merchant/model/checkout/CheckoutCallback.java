package com.midtrans.sdk.corekit.core.merchant.model.checkout;

import com.midtrans.sdk.corekit.base.callback.HttpRequestCallback;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.response.TokenResponse;

public interface CheckoutCallback extends HttpRequestCallback {

    void onSuccess(TokenResponse token);

    void onFailure(TokenResponse token, String reason);
}
