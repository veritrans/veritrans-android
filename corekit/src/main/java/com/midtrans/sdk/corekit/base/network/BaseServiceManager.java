package com.midtrans.sdk.corekit.base.network;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.midtrans.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.core.midtrans.response.SaveCardResponse;
import com.midtrans.sdk.corekit.core.midtrans.response.TokenDetailsResponse;
import com.midtrans.sdk.corekit.core.snap.SnapApiService;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.utilities.Constants;
import com.midtrans.sdk.corekit.utilities.Logger;

import retrofit.converter.ConversionException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_EMPTY_MERCHANT_URL;
import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_EMPTY_RESPONSE;
import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_FAILURE_RESPONSE;
import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_SNAP_TOKEN;

public abstract class BaseServiceManager {
    private static String TAG = BaseServiceManager.class.getSimpleName();
    Boolean isRunning;

    protected void releaseResources() {
        this.isRunning = false;
    }

    public <T> Boolean isSnapTokenAvailable(MidtransCallback<T> callback,
                                            String snapToken,
                                            SnapApiService apiService) {
        if (snapToken == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_SNAP_TOKEN));
            return false;
        }
        if (apiService == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_MERCHANT_URL));
            return false;
        }
        if (apiService != null && snapToken != null) {
            return true;
        }
        return null;
    }

    public void handleCallbackResponse(Call<BasePaymentResponse> basePaymentResponseCall, final MidtransCallback<BasePaymentResponse> basePaymentResponseMidtransCallback) {
        basePaymentResponseCall.enqueue(new Callback<BasePaymentResponse>() {
            @Override
            public void onResponse(@NonNull Call<BasePaymentResponse> call, @NonNull Response<BasePaymentResponse> response) {
                releaseResources();
                handleServerResponse(response, basePaymentResponseMidtransCallback, null);
            }

            @Override
            public void onFailure(@NonNull Call<BasePaymentResponse> call, @NonNull Throwable throwable) {
                releaseResources();
                handleServerResponse(null, basePaymentResponseMidtransCallback, throwable);
            }
        });
    }

    public <T> void handleServerResponse(Response<T> response,
                                         MidtransCallback<T> callback,
                                         Throwable throwable) {
        if (response != null && response.isSuccessful()) {
            if (response.code() != 204) {
                T responseBody = response.body();
                callback.onSuccess(responseBody);
            } else {
                callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_RESPONSE));
            }
        } else {
            if (throwable != null) {
                callback.onFailed(new Throwable(throwable.getMessage(), throwable.getCause()));
            } else {
                callback.onFailed(new Throwable(MESSAGE_ERROR_FAILURE_RESPONSE));
            }
        }
    }

    protected void doOnApiServiceUnAvailable(MidtransCallback callback) {
        String errorMessage = Constants.MESSAGE_ERROR_EMPTY_MERCHANT_URL;
        callback.onFailed(new Throwable(errorMessage));
    }

    protected void doOnInvalidDataSupplied(MidtransCallback callback) {
        releaseResources();
        callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_INVALID_DATA_SUPPLIED));
    }

}