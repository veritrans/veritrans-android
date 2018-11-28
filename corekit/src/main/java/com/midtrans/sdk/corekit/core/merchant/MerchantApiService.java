package com.midtrans.sdk.corekit.core.merchant;

import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.response.CheckoutResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MerchantApiService {

    /**
     * Get snap token
     *
     * @param requestModel SnapToken RequestModel
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("charge")
    Call<CheckoutResponse> checkout(@Body CheckoutTransaction requestModel);

}