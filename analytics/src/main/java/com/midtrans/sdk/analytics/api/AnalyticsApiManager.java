package com.midtrans.sdk.analytics.api;

import com.google.gson.Gson;

import android.util.Base64;

import com.midtrans.sdk.analytics.environment.BaseAnalyticsEnvironment;
import com.midtrans.sdk.analytics.models.Event;
import com.midtrans.sdk.analytics.utils.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rakawm on 4/21/17.
 */

public class AnalyticsApiManager {
    private final BaseAnalyticsEnvironment baseAnalyticsEnvironment;
    private final AnalyticsApi analyticsApi;

    public AnalyticsApiManager(BaseAnalyticsEnvironment baseAnalyticsEnvironment,
                               AnalyticsApi analyticsApi) {
        this.baseAnalyticsEnvironment = baseAnalyticsEnvironment;
        this.analyticsApi = analyticsApi;
    }

    public void trackEvent(final Event event) {
        String data = encodeToBase64(buildEventData(event));
        Call<Integer> call = analyticsApi.trackEvent(data);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Logger.debug("Analytics event sent. Name: " + event.getEvent());
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Logger.error("Analytics event failed to send. Name: " + event.getEvent(), t);
            }
        });
    }

    private String encodeToBase64(String json) {
        return Base64.encodeToString(json.getBytes(), Base64.DEFAULT);
    }

    private String buildEventData(Event event) {
        return buildGson().toJson(event);
    }

    private Gson buildGson() {
        return new Gson();
    }
}
