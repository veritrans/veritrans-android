package com.midtrans.sdk.coreflow.callback;

import com.midtrans.sdk.coreflow.models.snap.Token;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface CheckoutCallback {

    public void onSuccess(Token token);

    public void onFailure(Token token, String reason);

    public void onError(Throwable error);
}
