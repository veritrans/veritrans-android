package com.midtrans.sdk.corekit.callback;

import com.midtrans.sdk.corekit.models.snap.GoPayAuthorizationResponse;

/**
 * Created by ziahaqi on 9/12/17.
 */

public interface GoPayAuthorizationCallback {

    void onSuccess(GoPayAuthorizationResponse response);

    void onFailure(GoPayAuthorizationResponse response, String reason);

    void onError(Throwable error);
}
