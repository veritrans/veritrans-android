package com.midtrans.sdk.core;

/**
 * Created by rakawm on 10/19/16.
 */

public interface MidtransCoreCallback<T> {
    void onSuccess(T object);

    void onFailure(T object);

    void onError(Throwable throwable);
}
