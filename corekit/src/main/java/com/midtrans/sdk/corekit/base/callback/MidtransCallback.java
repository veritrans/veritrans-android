package com.midtrans.sdk.corekit.base.callback;

public interface MidtransCallback<T>{
    void onSuccess(T data);

    void onFailed(Throwable throwable);
}
