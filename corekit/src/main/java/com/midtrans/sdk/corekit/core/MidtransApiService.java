package com.midtrans.sdk.corekit.core;

import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by ziahaqi on 3/26/18.
 */

public interface MidtransApiService {

    /**
     * card_cvv, token_id, two_click, bank, secure, gross_amount this api call hit midtrans server
     *
     * @return callback of transaction response
     */
    @GET("token")
    Call<TokenDetailsResponse> getTokenTwoClick(
            @Query("card_cvv") String cardCVV,
            @Query("token_id") String tokenId,
            @Query("two_click") Boolean twoClick,
            @Query("secure") Boolean secure,
            @Query("gross_amount") Double grossAmount,
            @Query("bank") String bank,
            @Query("client_key") String clientKey,
            @Query("channel") String channel,
            @Query("type") String type,
            @Query("currency") String currency,
            @Query("point") Boolean point
    );

    @GET("token")
    Call<TokenDetailsResponse> get3DSToken(
            @Query("card_number") String cardNumber,
            @Query("card_cvv") String cardCVV,
            @Query("card_exp_month") String cardExpiryMonth,
            @Query("card_exp_year") String cardExpiryYear,
            @Query("client_key") String clientKey,
            @Query("bank") String bank,
            @Query("secure") Boolean secure,
            @Query("two_click") Boolean twoClick,
            @Query("gross_amount") Double grossAmount,
            @Query("channel") String channel,
            @Query("type") String type,
            @Query("currency") String currency,
            @Query("point") Boolean point
    );

    @GET("token")
    Call<TokenDetailsResponse> getToken(
            @Query("card_number") String cardNumber,
            @Query("card_cvv") String cardCVV,
            @Query("card_exp_month") String cardExpiryMonth,
            @Query("card_exp_year") String cardExpiryYear,
            @Query("client_key") String clientKey,
            @Query("gross_amount") Double grossAmount,
            @Query("channel") String channel,
            @Query("type") String type,
            @Query("currency") String currency,
            @Query("point") Boolean point
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

    @GET("token")
    Call<TokenDetailsResponse> getTokenInstalmentOfferTwoClick(
            @Query("card_cvv") String cardCVV,
            @Query("token_id") String tokenId,
            @Query("two_click") Boolean twoClick,
            @Query("secure") Boolean secure,
            @Query("gross_amount") Double grossAmount,
            @Query("bank") String bank,
            @Query("client_key") String clientKey,
            @Query("installment") Boolean instalment,
            @Query("installment_term") String instalmentTerm,
            @Query("channel") String channel,
            @Query("type") String type,
            @Query("currency") String currency,
            @Query("point") Boolean point
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

    @GET("token")
    Call<TokenDetailsResponse> get3DSTokenInstalmentOffers(
            @Query("card_number") String cardNumber,
            @Query("card_cvv") String cardCVV,
            @Query("card_exp_month") String cardExpiryMonth,
            @Query("card_exp_year") String cardExpiryYear,
            @Query("client_key") String clientKey,
            @Query("bank") String bank,
            @Query("secure") Boolean secure,
            @Query("two_click") Boolean twoClick,
            @Query("gross_amount") Double grossAmount,
            @Query("installment") Boolean instalment,
            @Query("channel") String channel,
            @Query("installment_term") String
                    instalmentTerm,
            @Query("type") String type,
            @Query("currency") String currency,
            @Query("point") Boolean point
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
    @GET("card/register")
    Call<CardRegistrationResponse> registerCard(
            @Query("card_number") String cardNumber,
            @Query("card_cvv") String cardCVV,
            @Query("card_exp_month") String cardExpiryMonth,
            @Query("card_exp_year") String cardExpiryYear,
            @Query("client_key") String clientKey
    );

}
