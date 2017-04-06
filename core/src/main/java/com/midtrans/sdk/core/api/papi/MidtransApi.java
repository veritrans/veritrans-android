package com.midtrans.sdk.core.api.papi;

import com.midtrans.sdk.core.models.papi.CardTokenResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by rakawm on 10/19/16.
 */

public interface MidtransApi {

    /**
     * Get card token for non secure transaction.
     *
     * @param cardNumber      card number.
     * @param cardCvv         card cvv number.
     * @param cardExpiryMonth card expiry month.
     * @param cardExpiryYear  card expiry year.
     * @param clientKey       Midtrans client key.
     * @param type            transaction type.
     * @param bank            acquiring bank.
     * @param installment     true if installment was enabled.
     * @param installmentTerm installment term.
     * @param channel channel.
     * @param point  point enabled.
     * @return token response.
     */
    @GET("token")
    Call<CardTokenResponse> getCardToken(
            @Query("card_number") String cardNumber,
            @Query("card_cvv") String cardCvv,
            @Query("card_exp_month") String cardExpiryMonth,
            @Query("card_exp_year") String cardExpiryYear,
            @Query("client_key") String clientKey,
            @Query("type") String type,
            @Query("bank") String bank,
            @Query("installment") boolean installment,
            @Query("installment_term") int installmentTerm,
            @Query("channel") String channel,
            @Query("point") boolean point
    );

    /**
     * Get card token for secure transaction.
     *
     * @param cardNumber      card number.
     * @param cardCvv         card cvv number.
     * @param cardExpiryMonth card expiry month.
     * @param cardExpiryYear  card expiry year.
     * @param secure          true if secure mode was enabled.
     * @param grossAmount     transaction amount.
     * @param clientKey       Midtrans client key.
     * @param type            transaction type.
     * @param bank            acquiring bank.
     * @param installment     true if installment was enabled.
     * @param installmentTerm installment term.
     * @param channel channel.
     * @param point  point enabled.
     * @return token response.
     */
    @GET("token")
    Call<CardTokenResponse> getCardToken(
            @Query("card_number") String cardNumber,
            @Query("card_cvv") String cardCvv,
            @Query("card_exp_month") String cardExpiryMonth,
            @Query("card_exp_year") String cardExpiryYear,
            @Query("secure") boolean secure,
            @Query("gross_amount") int grossAmount,
            @Query("client_key") String clientKey,
            @Query("type") String type,
            @Query("bank") String bank,
            @Query("installment") boolean installment,
            @Query("installment_term") int installmentTerm,
            @Query("channel") String channel,
            @Query("point") boolean point
    );

    /**
     * Get card token for two clicks transaction.
     *
     * @param cardCvv         card cvv number.
     * @param tokenId         saved token from previous transaction.
     * @param twoClick        true if two clicks mode was enabled.
     * @param grossAmount     transaction amount.
     * @param secure          true if secure mode was enabled.
     * @param clientKey       Midtrans client key.
     * @param type            transaction type.
     * @param bank            acquiring bank.
     * @param installment     true if installment was enabled.
     * @param installmentTerm installment term.
     * @param channel channel.
     * @param point  point enabled.
     * @return token response.
     */
    @GET("token")
    Call<CardTokenResponse> getCardToken(
            @Query("card_cvv") String cardCvv,
            @Query("token_id") String tokenId,
            @Query("two_click") boolean twoClick,
            @Query("gross_amount") int grossAmount,
            @Query("secure") boolean secure,
            @Query("client_key") String clientKey,
            @Query("type") String type,
            @Query("bank") String bank,
            @Query("installment") boolean installment,
            @Query("installment_term") int installmentTerm,
            @Query("channel") String channel,
            @Query("point") boolean point
    );
}
