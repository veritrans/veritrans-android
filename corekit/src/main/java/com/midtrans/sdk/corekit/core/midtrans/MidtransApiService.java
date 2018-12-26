package com.midtrans.sdk.corekit.core.midtrans;

import com.midtrans.sdk.corekit.core.midtrans.response.CardRegistrationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by ziahaqi on 3/26/18.
 */

public interface MidtransApiService {

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
