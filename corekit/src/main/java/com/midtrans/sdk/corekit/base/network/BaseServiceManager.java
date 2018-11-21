package com.midtrans.sdk.corekit.base.network;

import com.midtrans.sdk.corekit.base.callback.HttpRequestCallback;
import com.midtrans.sdk.corekit.utilities.Constants;

public abstract class BaseServiceManager {
    private static String TAG = BaseServiceManager.class.getSimpleName();
    Boolean isRunning;

    protected void releaseResources() {
        this.isRunning = false;
    }

    protected void doOnInvalidDataSupplied(HttpRequestCallback callback) {
        releaseResources();
        callback.onError(new Throwable(Constants.MESSAGE_ERROR_INVALID_DATA_SUPPLIED));
    }

    protected void doOnApiServiceUnAvailable(HttpRequestCallback callback) {
        String errorMessage = Constants.MESSAGE_ERROR_EMPTY_MERCHANT_URL;
        callback.onError(new Throwable(errorMessage));
    }
}