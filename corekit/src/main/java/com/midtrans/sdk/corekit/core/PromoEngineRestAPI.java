package com.midtrans.sdk.corekit.core;

import com.midtrans.sdk.corekit.models.promo.ObtainPromoResponse;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by rakawm on 2/3/17.
 */

public interface PromoEngineRestAPI {
    @POST("/v2/promo/obtain_promo")
    void obtainPromo(
            @Query("promo_id") String promoId,
            @Query("amount") double amount,
            @Query("client_key") String clientKey,
            Callback<ObtainPromoResponse> callback
    );
}
