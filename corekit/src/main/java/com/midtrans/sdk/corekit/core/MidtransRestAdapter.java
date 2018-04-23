package com.midtrans.sdk.corekit.core;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.midtrans.sdk.corekit.BuildConfig;
import com.midtrans.sdk.corekit.models.snap.params.CreditCardPaymentParams;
import com.midtrans.sdk.corekit.utilities.CustomTypeAdapter;

import java.io.IOException;
import java.sql.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chetan on 16/10/15.
 */
public class MidtransRestAdapter {
    private static final String TAG = MidtransRestAdapter.class.getName();

    /**
     * Create Merchant API implementation
     *
     * @param merchantBaseUrl Merchant base URL
     * @return Merchant API implementation
     */
    public static MerchantApiService newMerchantApiService(String merchantBaseUrl, int timeout) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(merchantBaseUrl)
                .client(newOkHttpClient(timeout))
                .addConverterFactory(GsonConverterFactory.create(newGson()))
                .build();

        return retrofit.create(MerchantApiService.class);
    }

    /**
     * It will return instance of PaymentAPI using that we can execute api calls.
     *
     * @return Payment API implementation
     */
    public static MidtransApiService newMidtransApiService(int timeout) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(newOkHttpClient(timeout))
                .addConverterFactory(GsonConverterFactory.create(newGson()))
                .build();

        return retrofit.create(MidtransApiService.class);
    }


    public static SnapApiService newSnapApiService(int timeout) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SNAP_BASE_URL)
                .client(newSnapOkHttpClient(timeout))
                .addConverterFactory(GsonConverterFactory.create(newGson()))
                .build();

        return retrofit.create(SnapApiService.class);
    }


    private static HttpLoggingInterceptor newHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor
                .setLevel(Logger.enabled ?
                        HttpLoggingInterceptor.Level.BODY :
                        HttpLoggingInterceptor.Level.NONE);
        return httpLoggingInterceptor;
    }

    private static Gson newGson() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .registerTypeAdapter(CreditCardPaymentParams.class, new CustomTypeAdapter())
                .create();

        return gson;
    }

    private static Interceptor newHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request headerInterceptedRequest = request.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .build();

                return chain.proceed(headerInterceptedRequest);
            }
        };
    }

    private static Interceptor newSnapHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request headerInterceptedRequest = request.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .addHeader("X-Source", "mobile-android")
                        .addHeader("X-SDK-Version", "android-" + BuildConfig.VERSION_NAME)
                        .build();

                return chain.proceed(headerInterceptedRequest);
            }
        };
    }


    private static OkHttpClient newSnapOkHttpClient(int timeout) {
        return new OkHttpClient.Builder()
                .addInterceptor(newHttpLoggingInterceptor())
                .addInterceptor(newSnapHeaderInterceptor())
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .build();
    }

    private static OkHttpClient newOkHttpClient(int timeout) {
        return new OkHttpClient.Builder()
                .addInterceptor(newHttpLoggingInterceptor())
                .addInterceptor(newHeaderInterceptor())
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .build();
    }

}