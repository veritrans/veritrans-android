package com.midtrans.sdk.corekit.core.api.midtrans;

import com.midtrans.sdk.corekit.core.api.midtrans.model.registration.TokenizeResponse;
import com.midtrans.sdk.corekit.core.api.midtrans.model.tokendetails.TokenDetailsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import static com.midtrans.sdk.corekit.utilities.Constants.X_AUTH;

public interface MidtransApiService {

    String GET_TOKEN = "token";
    String CARD_REGISTER = "card/register";

    String HEADER_X_AUTH = X_AUTH + ": da53847171259b511488cf366e701050";

    /**
     * card_cvv, token_id, two_click, bank, secure, gross_amount this api call hit midtrans server
     *
     * @return callback of transaction response
     */
    @GET(GET_TOKEN)
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

    @GET(GET_TOKEN)
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

    @GET(GET_TOKEN)
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

    @GET(GET_TOKEN)
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
    @GET(GET_TOKEN)
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
            @Query("installment_term") String instalmentTerm,
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
    @Headers({HEADER_X_AUTH})
    @GET(CARD_REGISTER)
    Call<TokenizeResponse> tokenizeCard(
            @Query("card_number") String cardNumber,
            @Query("card_cvv") String cardCVV,
            @Query("card_exp_month") String cardExpiryMonth,
            @Query("card_exp_year") String cardExpiryYear,
            @Query("client_key") String clientKey
    );

}