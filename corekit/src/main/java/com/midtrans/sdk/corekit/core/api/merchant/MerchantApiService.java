package com.midtrans.sdk.corekit.core.api.merchant;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.response.CheckoutWithTransactionResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.SaveCardRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MerchantApiService {

    String CHECKOUT_TRANSACTION = "charge";
    String SAVE_CARD = "users/{user_id}/tokens";

    /**
     * Get snap token
     *
     * @param requestModel SnapToken RequestModel
     */
    @POST(CHECKOUT_TRANSACTION)
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<CheckoutWithTransactionResponse> checkout(@Body CheckoutTransaction requestModel);

    /**
     * save cards to merchant server
     *
     * @param userId            unique id for every user
     * @param saveCardsRequests list of saved credit cards
     */
    @POST(SAVE_CARD)
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<List<SaveCardRequest>> saveCards(@Path("user_id") String userId,
                                          @Body List<SaveCardRequest> saveCardsRequests);

    /**
     * save cards to merchant server
     *
     * @param userId unique id for every user
     *
     */
    @GET(SAVE_CARD)
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<List<SaveCardRequest>> getCards(@Path("user_id") String userId);
}