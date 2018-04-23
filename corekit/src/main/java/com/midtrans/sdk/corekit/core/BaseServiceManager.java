package com.midtrans.sdk.corekit.core;

import com.midtrans.sdk.corekit.callback.HttpRequestCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.models.SaveCardResponse;

import retrofit.converter.ConversionException;

/**
 * Created by ziahaqi on 3/27/18.
 */

public abstract class BaseServiceManager {
    private static String TAG = BaseServiceManager.class.getSimpleName();
    Boolean isRunning;

    protected void releaseResources() {
        this.isRunning = false;
    }

    protected void doOnResponseFailure(Throwable error, HttpRequestCallback callback) {

        releaseResources();
        try {
            Logger.e(TAG, "Error > cause:" + error.getCause() + "| message:" + error.getMessage());

            if (callback instanceof SaveCardCallback && error.getCause() instanceof ConversionException) {

                SaveCardResponse saveCardResponse = new SaveCardResponse();
                saveCardResponse.setCode(200);
                saveCardResponse.setMessage(error.getMessage());
                ((SaveCardCallback) callback).onSuccess(saveCardResponse);
                return;
            }
            callback.onError(error);

        } catch (Exception e) {
            callback.onError(new Throwable(e.getMessage(), e.getCause()));
        }
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
