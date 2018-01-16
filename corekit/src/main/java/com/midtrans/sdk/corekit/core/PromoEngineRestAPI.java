package com.midtrans.sdk.corekit.core;

import com.midtrans.sdk.corekit.models.promo.HoldPromoResponse;
import com.midtrans.sdk.corekit.models.promo.ObtainPromoResponse;
import com.midtrans.sdk.corekit.models.promo.ObtainPromosResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by rakawm on 2/3/17.
 */

public interface PromoEngineRestAPI {
    @Deprecated
    @POST("/v2/promo/obtain_promo")
    void obtainPromo(
            @Query("promo_id") String promoId,
            @Query("amount") double amount,
            @Query("client_key") String clientKey,
            Callback<ObtainPromoResponse> callback
    );

    /**
     * @param clientKey   merchant client key
     * @param totalAmount total amount
     * @param cardToken   savedCardToken
     * @param promoCode   promoCode
     * @param callback    promos callback
     */
    @GET("/promo/v3/promos")
    void obtainPromoByCardToken(
            @Query("promo_code") String promoCode,
            @Query("amount") double totalAmount,
            @Query("client_key") String clientKey,
            @Query("token") String cardToken,
            Callback<ObtainPromosResponse> callback
    );

    /**
     * @param clientKey   merchant client key
     * @param cardNumber  credit card number
     * @param totalAmount total amount
     * @param promoCode   promoCode
     * @param callback    promos callback
     */
    @GET("/promo/v3/promos")
    void obtainPromoByCardNumber(
            @Query("promo_code") String promoCode,
            @Query("amount") double totalAmount,
            @Query("client_key") String clientKey,
            @Query("card") String cardNumber,
            Callback<ObtainPromosResponse> callback
    );

    /**
     * @param promoToken
     * @param totalAmount
     * @param clientKey
     * @param callback
     */
    @GET("/promo/v3/promos/hold")
    void holdPromo(
            @Query("promo_token") String promoToken,
            @Query("amount") long totalAmount,
            @Query("client_key") String clientKey,
            Callback<HoldPromoResponse> callback
    );
}
