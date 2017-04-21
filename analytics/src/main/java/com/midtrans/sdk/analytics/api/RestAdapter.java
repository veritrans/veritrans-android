package com.midtrans.sdk.analytics.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.midtrans.sdk.analytics.AnalyticsEnvironment;
import com.midtrans.sdk.analytics.utils.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rakawm on 4/21/17.
 */

public class RestAdapter {
    private static final String BASE_URL = "https://api.mixpanel.com/";
    private static final int TIMEOUT_IN_SECOND = 30;
    private final AnalyticsEnvironment environment;

    public RestAdapter(AnalyticsEnvironment environment) {
        this.environment = environment;
    }

    public AnalyticsApi getAnalyticsApi() {
        return buildAnalyticsApiRetrofit().create(AnalyticsApi.class);
    }

    private Retrofit buildAnalyticsApiRetrofit() {
        return new Retrofit.Builder()
                .client(buildOkHttpClient())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .build();
    }

    private OkHttpClient buildOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(buildHttpLoggingInterceptor())
                .connectTimeout(TIMEOUT_IN_SECOND, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_IN_SECOND, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_IN_SECOND, TimeUnit.SECONDS)
                .build();
    }

    private Interceptor buildHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor
                .setLevel(Logger.enabled ?
                        HttpLoggingInterceptor.Level.BODY :
                        HttpLoggingInterceptor.Level.NONE);
        return httpLoggingInterceptor;
    }

    private Gson buildGson() {
        return new GsonBuilder()
                .create();
    }
}
