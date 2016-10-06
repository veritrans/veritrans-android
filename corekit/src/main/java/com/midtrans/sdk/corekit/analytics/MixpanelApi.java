package com.midtrans.sdk.corekit.analytics;


import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author rakawm
 */
public interface MixpanelApi {
    @GET("/track")
    void trackEvent(
            @Query("data") String data, Callback<Integer> callback
    );
}
