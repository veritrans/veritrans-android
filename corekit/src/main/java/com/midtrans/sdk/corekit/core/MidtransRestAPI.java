package com.midtrans.sdk.corekit.core;

import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionCancelResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by ziahaqi on 27/06/2016.
 */
public interface MidtransRestAPI {


    /*
     *  PAPI end point
     */

    //http://api.sandbox.veritrans.co.id/v2/10938010/cancel/
    @Headers({"Content-Type: application/json",
            "Accept: application/json"})
    @POST("/{id}/cancel/")
    void cancelTransaction(
            @Header("x-auth") String auth,
            @Path("id") String transactionId,
            Callback<TransactionCancelResponse> callback);

    /**
     * card_cvv, token_id, two_click, bank, secure, gross_amount this api call hit midtrans server
     *
     * @return callback of transaction response
     */
    @GET("/token/")
    void getTokenTwoClick(
            @Query("card_cvv") String cardCVV,
            @Query("token_id") String tokenId,
            @Query("two_click") boolean twoClick,
            @Query("secure") boolean secure,
            @Query("gross_amount") double grossAmount,
            @Query("bank") String bank,
            @Query("client_key") String clientKey, Callback<TokenDetailsResponse> callback);

    @GET("/token/")
    void get3DSToken(@Query("card_number") String cardNumber,
                     @Query("card_cvv") String cardCVV,
                     @Query("card_exp_month") String cardExpiryMonth,
                     @Query("card_exp_year") String cardExpiryYear,
                     @Query("client_key") String clientKey,
                     @Query("bank") String bank,
                     @Query("secure") boolean secure,
                     @Query("two_click") boolean twoClick,
                     @Query("gross_amount") double grossAmount,
                     Callback<TokenDetailsResponse> callback
    );

    @GET("/token/")
    void getToken(
            @Query("card_number") String cardNumber,
            @Query("card_cvv") String cardCVV,
            @Query("card_exp_month") String cardExpiryMonth,
            @Query("card_exp_year") String cardExpiryYear,
            @Query("client_key") String clientKey,
            Callback<TokenDetailsResponse> callback
    );

    /**
     * For instalment offers get token
     *
     * @param cardCVV        card cvv number
     * @param tokenId        token identifier
     * @param twoClick       is two click or not
     * @param secure         is secure or not
     * @param grossAmount    gross amount
     * @param bank           bank name
     * @param clientKey      client key
     * @param instalment     installment
     * @param instalmentTerm installment terms
     * @return callback of transaction response
     */

    @GET("/token/")
    void getTokenInstalmentOfferTwoClick(
            @Query("card_cvv") String cardCVV,
            @Query("token_id") String tokenId,
            @Query("two_click") boolean twoClick,
            @Query("secure") boolean secure,
            @Query("gross_amount") double grossAmount,
            @Query("bank") String bank,
            @Query("client_key") String clientKey,
            @Query("installment") boolean instalment,
            @Query("installment_term") String instalmentTerm,
            Callback<TokenDetailsResponse> callback
    );

    /***
     * Get instalment offers 3ds token
     *
     * @param cardNumber      card number
     * @param cardCVV         card cvv number
     * @param cardExpiryMonth card expiry's month
     * @param cardExpiryYear  card expiry's year
     * @param clientKey       client key
     * @param bank            bank name
     * @param secure          is secure
     * @param twoClick        is two click
     * @param grossAmount     gross amount
     * @return callback of transaction response
     */

    @GET("/token/")
    void get3DSTokenInstalmentOffers(@Query("card_number") String cardNumber,
                                     @Query("card_cvv") String cardCVV,
                                     @Query("card_exp_month") String cardExpiryMonth,
                                     @Query("card_exp_year") String cardExpiryYear,
                                     @Query("client_key") String clientKey,
                                     @Query("bank") String bank,
                                     @Query("secure") boolean secure,
                                     @Query("two_click") boolean twoClick,
                                     @Query("gross_amount") double grossAmount,
                                     @Query("installment") boolean instalment,
                                     @Query("installment_term") String
                                             instalmentTerm,
                                     Callback<TokenDetailsResponse> callback
    );


    /**
     * Register card into Veritrans API.
     *
     * @param cardNumber      credit card number
     * @param cardCVV         credit card cvv number
     * @param cardExpiryMonth credit card expiry month in number
     * @param cardExpiryYear  credit card expiry year in 4 digit (example: 2020)
     * @param clientKey       midtrans API client key
     * @return callback of token
     */
    @Headers({"Content-Type: application/json", "x-auth: da53847171259b511488cf366e701050"})
    @GET("/card/register")
    void registerCard(
            @Query("card_number") String cardNumber,
            @Query("card_cvv") String cardCVV,
            @Query("card_exp_month") String cardExpiryMonth,
            @Query("card_exp_year") String cardExpiryYear,
            @Query("client_key") String clientKey, Callback<CardRegistrationResponse> callback
    );
}
