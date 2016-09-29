package com.midtrans.sdk.corekit.callback;

import com.midtrans.sdk.corekit.models.TokenDetailsResponse;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface CardTokenCallback {

    void onSuccess(TokenDetailsResponse response);

    void onFailure(TokenDetailsResponse response, String reason);

    void onError(Throwable error);
}
