package com.midtrans.sdk.core.api;

import com.midtrans.sdk.core.Environment;
import com.midtrans.sdk.core.api.merchant.MerchantApi;
import com.midtrans.sdk.core.api.papi.MidtransApi;
import com.midtrans.sdk.core.api.snap.SnapApi;
import com.midtrans.sdk.core.utils.Logger;
import com.midtrans.sdk.core.utils.PaymentUtilities;
import com.midtrans.sdk.core.utils.Utilities;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rakawm on 10/19/16.
 */

public class RestAdapter {
    private static final int TIMEOUT_IN_SECOND = 30;
    private final Environment environment;

    public RestAdapter(Environment environment) {
        this.environment = environment;
    }

    public MidtransApi getMidtransApiClient() {
        return buildPaymentApiRetrofit().create(MidtransApi.class);
    }

    public SnapApi getSnapApiClient() {
        return buildSnapApiRetrofit().create(SnapApi.class);
    }

    public MerchantApi getMerchantApiClient(String checkoutUrl) {
        return buildMerchantApiRetrofit(checkoutUrl).create(MerchantApi.class);
    }

    private Retrofit buildPaymentApiRetrofit() {
        return new Retrofit.Builder()
                .client(buildOkHttpClient())
                .baseUrl(PaymentUtilities.getPaymentBaseUrl(environment))
                .addConverterFactory(GsonConverterFactory.create(Utilities.buildGson()))
                .build();
    }

    private Retrofit buildSnapApiRetrofit() {
        return new Retrofit.Builder()
                .client(buildOkHttpClient())
                .baseUrl(PaymentUtilities.getSnapBaseUrl(environment))
                .addConverterFactory(GsonConverterFactory.create(Utilities.buildGson()))
                .build();
    }

    private Retrofit buildMerchantApiRetrofit(String checkoutUrl) {
        return new Retrofit.Builder()
                .baseUrl(checkoutUrl)
                .client(buildOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(Utilities.buildGson()))
                .build();
    }

    private OkHttpClient buildOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(buildHttpLoggingInterceptor())
                .addInterceptor(new HeaderInterceptor())
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
}
