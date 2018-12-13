package com.midtrans.sdk.corekit.core.merchant;

import android.support.annotation.NonNull;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.network.BaseServiceManager;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.response.CheckoutWithTransactionResponse;
import com.midtrans.sdk.corekit.utilities.Constants;

import retrofit2.Call;

public class MerchantApiManager extends BaseServiceManager {

    private static final String TAG = "MerchantApiManager";

    private MerchantApiService apiService;

    public MerchantApiManager(MerchantApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * This method will create a HTTP request to Merchant Server for making Snap Token.
     *
     * @param request  CheckoutTransaction model that construct by user.
     * @param callback callback of making Snap Token.
     */
    public void checkout(final CheckoutTransaction request,
                         final MidtransCallback<CheckoutWithTransactionResponse> callback) {
        if (request == null) {
            callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        } else {
            if (apiService == null) {
                callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_EMPTY_MERCHANT_URL));
            } else {
                Call<CheckoutWithTransactionResponse> call = apiService.checkout(request);
                call.enqueue(new retrofit2.Callback<CheckoutWithTransactionResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CheckoutWithTransactionResponse> call, @NonNull retrofit2.Response<CheckoutWithTransactionResponse> response) {
                        releaseResources();
                        handleServerResponse(response, callback, null);
                    }

                    @Override
                    public void onFailure(@NonNull Call<CheckoutWithTransactionResponse> call, @NonNull Throwable throwable) {
                        releaseResources();
                        handleServerResponse(null, callback, throwable);
                    }
                });
            }
        }
    }
}