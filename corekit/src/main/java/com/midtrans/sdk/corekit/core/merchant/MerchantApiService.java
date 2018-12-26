package com.midtrans.sdk.corekit.core.merchant;

import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.response.CheckoutWithTransactionResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.creditcard.SaveCardRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MerchantApiService {

    /**
     * Get snap token
     *
     * @param requestModel SnapToken RequestModel
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("charge")
    Call<CheckoutWithTransactionResponse> checkout(@Body CheckoutTransaction requestModel);

    /**
     * get cards from merchant server
     *
     * @param userId unique id for every user
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("users/{user_id}/tokens")
    Call<List<SaveCardRequest>> getCards(@Path("user_id") String userId);

    /**
     * save cards to merchant server
     *
     * @param userId            unique id for every user
     * @param saveCardsRequests list of saved credit cards
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("users/{user_id}/tokens")
    Call<List<SaveCardRequest>> saveCards(@Path("user_id") String userId, @Body List<SaveCardRequest> saveCardsRequests);
}