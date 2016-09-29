package com.midtrans.sdk.corekit.callback;

import com.midtrans.sdk.corekit.models.snap.Token;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface CheckoutCallback {

    void onSuccess(Token token);

    void onFailure(Token token, String reason);

    void onError(Throwable error);
}
