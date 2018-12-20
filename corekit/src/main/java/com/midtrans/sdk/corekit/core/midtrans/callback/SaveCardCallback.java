package com.midtrans.sdk.corekit.core.midtrans.callback;

import com.midtrans.sdk.corekit.base.callback.HttpRequestCallback;
import com.midtrans.sdk.corekit.core.midtrans.response.SaveCardResponse;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface SaveCardCallback extends HttpRequestCallback {

    void onSuccess(SaveCardResponse response);

    void onFailure(String reason);
}
