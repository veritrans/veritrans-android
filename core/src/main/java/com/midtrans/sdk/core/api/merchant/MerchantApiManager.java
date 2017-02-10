package com.midtrans.sdk.core.api.merchant;

import android.text.TextUtils;

import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenRequest;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by rakawm on 10/19/16.
 */

public class MerchantApiManager {
    private final MerchantApi merchantApi;

    public MerchantApiManager(MerchantApi merchantApi) {
        this.merchantApi = merchantApi;
    }

    public void checkout(String checkoutUrl, final CheckoutTokenRequest checkoutTokenRequest, final MidtransCoreCallback<CheckoutTokenResponse> callback) {
        Call<CheckoutTokenResponse> responseCall = merchantApi.checkout(
                checkoutUrl,
                checkoutTokenRequest);
        responseCall.enqueue(new Callback<CheckoutTokenResponse>() {
            @Override
            public void onResponse(Call<CheckoutTokenResponse> call, Response<CheckoutTokenResponse> response) {
                handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<CheckoutTokenResponse> call, Throwable throwable) {
                callback.onError(throwable);
            }
        });
    }

    private void handleResponse(Response<CheckoutTokenResponse> response, MidtransCoreCallback<CheckoutTokenResponse> callback) {
        if (response.isSuccessful()
                && !TextUtils.isEmpty(response.body().token)) {
            callback.onSuccess(response.body());
        } else {
            callback.onFailure(response.body());
        }
    }
}
