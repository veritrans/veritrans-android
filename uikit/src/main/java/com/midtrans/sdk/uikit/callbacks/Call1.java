package com.midtrans.sdk.uikit.callbacks;

public abstract class Call1<T> {
    public abstract void onSuccess(T result);
    public void onError(Throwable t){}
}
