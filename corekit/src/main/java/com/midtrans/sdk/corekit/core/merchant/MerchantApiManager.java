package com.midtrans.sdk.corekit.core.merchant;

import android.support.annotation.NonNull;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.network.BaseServiceManager;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.TransactionRequest;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.response.CheckoutResponse;
import com.midtrans.sdk.corekit.utilities.Constants;

import retrofit2.Call;
import retrofit2.Response;

import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_EMPTY_RESPONSE;

public class MerchantApiManager extends BaseServiceManager {

    private static final String TAG = "MerchantApiManager";

    private MerchantApiService apiService;

    public MerchantApiManager(MerchantApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * This method will create a HTTP request to Merchant Server for making Snap Token.
     *
     * @param request  TransactionRequest model that construct by user.
     * @param callback callback of making Snap Token.
     */
    public void checkout(final TransactionRequest request,
                         final MidtransCallback<CheckoutResponse> callback) {
        if (request == null) {
            callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        } else {
            if (apiService == null) {
                callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_EMPTY_MERCHANT_URL));
            } else {
                Call<CheckoutResponse> call = apiService.checkout(request);
                call.enqueue(new retrofit2.Callback<CheckoutResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CheckoutResponse> call, @NonNull retrofit2.Response<CheckoutResponse> response) {
                        releaseResources();
                        handleServerResponse(response, callback, null);
                    }

                    @Override
                    public void onFailure(@NonNull Call<CheckoutResponse> call, @NonNull Throwable throwable) {
                        releaseResources();
                        handleServerResponse(null, callback, throwable);
                    }
                });
            }
        }
    }

    private <T> void handleServerResponse(Response<T> response,
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
                callback.onFailed(new Throwable(MESSAGE_ERROR_EMPTY_RESPONSE));
            }
        }
    }

}