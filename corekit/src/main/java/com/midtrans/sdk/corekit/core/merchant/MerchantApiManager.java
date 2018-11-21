package com.midtrans.sdk.corekit.core.merchant;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.network.BaseServiceManager;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.CheckoutCallback;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.TransactionRequest;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.response.TokenResponse;
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
     * @param request  TransactionRequest model that construct by user.
     * @param callback callback of making Snap Token.
     */
    public void checkout(@NonNull final TransactionRequest request,
                         final CheckoutCallback callback) {
        if (apiService == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<TokenResponse> call = apiService.checkout(request);
        call.enqueue(new retrofit2.Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, retrofit2.Response<TokenResponse> response) {
                releaseResources();
                TokenResponse token = response.body();
                if (token != null) {
                    if (token.getSnapToken() != null && !TextUtils.isEmpty(token.getSnapToken())) {
                        callback.onSuccess(token);
                    } else {
                        callback.onFailure(token, Constants.MESSAGE_ERROR_EMPTY_RESPONSE);
                    }
                } else {
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                releaseResources();
                callback.onError(new Throwable(t.getMessage(), t.getCause()));
            }
        });
    }

}