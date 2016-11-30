package com.midtrans.sdk.corekit.core;

import com.midtrans.sdk.corekit.models.CardTransfer;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.TokenRequestModel;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.Token;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by ziahaqi on 27/06/2016.
 */
public interface MerchantRestAPI {

    /**
     * Do the payment using credit card
     *
     * @param auth         token
     * @param cardTransfer card request body
     * @param callback     transaction response
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingCard(
            @Header("x-auth") String auth, @Body CardTransfer
            cardTransfer, Callback<TransactionResponse> callback);

    /**
     * Get snap token.
     *
     * @param requestModel SnapToken RequestModel
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge")
    void checkout(@Body TokenRequestModel requestModel, Callback<Token> callback);

    /**
     * save cards to merchant server
     *
     * @param userId            unique id for every user
     * @param saveCardsRequests list of saved credit cards
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/users/{user_id}/tokens")
    void saveCards(@Path("user_id") String userId, @Body ArrayList<SaveCardRequest> saveCardsRequests,
                   Callback<String> callback);

    /**
     * get cards from merchant server
     *
     * @param userId unique id for every user
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/users/{user_id}/tokens")
    void getCards(@Path("user_id") String userId, Callback<ArrayList<SaveCardRequest>> callback);
}
