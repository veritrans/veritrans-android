package com.midtrans.sdk.coreflow.callback;

import com.midtrans.sdk.coreflow.models.TokenDetailsResponse;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface CardTokenCallback {

    public void onSuccess(TokenDetailsResponse response);

    public void onFailure(TokenDetailsResponse response, String reason);

    public void onError(Throwable error);
}
