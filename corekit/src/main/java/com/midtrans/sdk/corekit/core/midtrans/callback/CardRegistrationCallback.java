package com.midtrans.sdk.corekit.core.midtrans.callback;

import com.midtrans.sdk.corekit.base.callback.HttpRequestCallback;
import com.midtrans.sdk.corekit.core.midtrans.response.CardRegistrationResponse;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface CardRegistrationCallback extends HttpRequestCallback {

    void onSuccess(CardRegistrationResponse response);

    void onFailure(CardRegistrationResponse response, String reason);
}
