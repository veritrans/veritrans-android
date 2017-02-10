package com.midtrans.sdk.core.api.merchant;

import com.midtrans.sdk.core.models.merchant.CheckoutTokenRequest;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by rakawm on 10/19/16.
 */

public interface MerchantApi {
    /**
     * Checkout to Snap backend via merchant server.
     *
     * @param checkoutTokenRequest checkout token request.
     * @return checkout response.
     */
    @POST
    Call<CheckoutTokenResponse> checkout(
            @Url String url,
            @Body CheckoutTokenRequest checkoutTokenRequest
    );
}
