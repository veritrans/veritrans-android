package com.midtrans.sdk.corekit.core.midtrans.callback;

import com.midtrans.sdk.corekit.base.callback.HttpRequestCallback;
import com.midtrans.sdk.corekit.core.midtrans.response.TokenDetailsResponse;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface CardTokenCallback extends HttpRequestCallback {

    void onSuccess(TokenDetailsResponse response);

    void onFailure(TokenDetailsResponse response, String reason);
}
