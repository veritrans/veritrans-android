package com.midtrans.sdk.corekit.callback;

/**
 * Created by rakawm on 2/28/17.
 */

public interface DeleteCardCallback {
    void onSuccess(Void object);

    void onFailure(Void object);

    void onError(Throwable throwable);
}
