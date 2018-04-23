package com.midtrans.sdk.corekit.callback;

import com.midtrans.sdk.corekit.models.GoPayResendAuthorizationResponse;

/**
 * Created by ziahaqi on 9/12/17.
 */

public interface GoPayResendAuthorizationCallback extends HttpRequestCallback{
    void onSuccess(GoPayResendAuthorizationResponse response);

    void onFailure(GoPayResendAuthorizationResponse response, String reason);

    void onError(Throwable error);
}
