package com.midtrans.sdk.corekit.core;

import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.TokenRequestModel;
import com.midtrans.sdk.corekit.models.snap.Token;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ziahaqi on 3/26/18.
 */

public interface MerchantApiService {

    /**
     * Get snap token.
     *
     * @param requestModel SnapToken RequestModel
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("charge")
    Call<Token> checkout(@Body TokenRequestModel requestModel);

    /**
     * save cards to merchant server
     *
     * @param userId            unique id for every user
     * @param saveCardsRequests list of saved credit cards
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("users/{user_id}/tokens")
    Call<String> saveCards(@Path("user_id") String userId, @Body List<SaveCardRequest> saveCardsRequests);

    /**
     * get cards from merchant server
     *
     * @param userId unique id for every user
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("users/{user_id}/tokens")
    Call<List<SaveCardRequest>> getCards(@Path("user_id") String userId);
}
