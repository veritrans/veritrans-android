package com.midtrans.sdk.corekit.base.callback;

public interface MidtransCallback<T> extends HttpRequestCallback{
    void onSuccess(T data);

    void onFailed(Throwable throwable);
}
