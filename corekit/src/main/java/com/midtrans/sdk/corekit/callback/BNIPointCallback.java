package com.midtrans.sdk.corekit.callback;

/**
 * Created by ziahaqi on 1/12/17.
 */

public interface BNIPointCallback {

    void onSuccess(long response);

    void onFailure(String reason);

    void onError(Throwable error);
}
