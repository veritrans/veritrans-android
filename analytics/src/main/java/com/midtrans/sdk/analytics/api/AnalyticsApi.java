package com.midtrans.sdk.analytics.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by rakawm on 4/21/17.
 */

public interface AnalyticsApi {
    /**
     * Track analytics event REST API interface.
     *
     * @param data base64 string encoded data.
     */
    @GET("track")
    Call<Integer> trackEvent(
            @Query("data") String data
    );
}
