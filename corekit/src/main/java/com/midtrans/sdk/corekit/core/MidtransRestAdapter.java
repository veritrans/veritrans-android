package com.midtrans.sdk.corekit.core;

import android.text.TextUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.jakewharton.retrofit.Ok3Client;
import com.midtrans.sdk.corekit.models.snap.params.CreditCardPaymentParams;
import com.midtrans.sdk.corekit.utilities.CustomTypeAdapter;
import com.squareup.okhttp.OkHttpClient;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by chetan on 16/10/15.
 */
public class MidtransRestAdapter {
    private static final RestAdapter.LogLevel LOG_LEVEL = Logger.enabled ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE;
    private static final String TAG = MidtransRestAdapter.class.getName();

    /**
     * It will return instance of PaymentAPI using that we can execute api calls.
     *
     * @param baseUrl base URL of PAPI
     * @return Payment API implementation
     */
    public static MidtransRestAPI getVeritransApiClient(String baseUrl, int timeout) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(timeout, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(timeout, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(timeout, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(LOG_LEVEL)
                .setRequestInterceptor(buildMidtransRequestInterceptor())
                .setClient(new OkClient(okHttpClient));
        RestAdapter restAdapter = builder.build();
        return restAdapter.create(MidtransRestAPI.class);
    }

    /**
     * Create Merchant API implementation
     *
     * @param merchantBaseURL Merchant base URL
     * @return Merchant API implementation
     */
    public static MerchantRestAPI getMerchantApiClient(String merchantBaseURL, int timeout) {

        if (TextUtils.isEmpty(merchantBaseURL)) {
            return null;
        }

        okhttp3.OkHttpClient.Builder okclient = new okhttp3.OkHttpClient.Builder();
        okclient.connectTimeout(timeout, TimeUnit.SECONDS);
        okclient.readTimeout(timeout, TimeUnit.SECONDS);
        okclient.writeTimeout(timeout, TimeUnit.SECONDS);

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setLogLevel(LOG_LEVEL)
                .setClient(new Ok3Client(okclient.build()))
                .setRequestInterceptor(buildSnapRequestInterceptor())
                .setEndpoint(merchantBaseURL);
        RestAdapter restAdapter = builder.build();
        return restAdapter.create(MerchantRestAPI.class);

    }

    /**
     * Crate Snap API
     *
     * @param snapBaseURL base URL of snap API
     * @return snap API implementation
     */
    public static SnapRestAPI getSnapRestAPI(String snapBaseURL, int timeOut) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(timeOut, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(timeOut, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(timeOut, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .registerTypeAdapter(CreditCardPaymentParams.class, new CustomTypeAdapter())
                .create();
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setLogLevel(LOG_LEVEL)
                .setClient(new OkClient(okHttpClient))
                .setRequestInterceptor(buildSnapRequestInterceptor())
                .setEndpoint(snapBaseURL);
        return builder.build().create(SnapRestAPI.class);
    }

    public static PromoEngineRestAPI getPromoEngineRestAPI(String promoEngineUrl, int timeOut) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(timeOut, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(timeOut, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(timeOut, TimeUnit.SECONDS);
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setLogLevel(LOG_LEVEL)
                .setClient(new OkClient(okHttpClient))
                .setRequestInterceptor(buildSnapRequestInterceptor())
                .setEndpoint(promoEngineUrl);
        return builder.build().create(PromoEngineRestAPI.class);
    }

    private static RequestInterceptor buildSnapRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("X-Source", "mobile-android");
                request.addHeader("Accept", "application/json");
                request.addHeader("Content-Type", "application/json");
            }
        };
    }

    private static RequestInterceptor buildMidtransRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
                request.addHeader("Content-Type", "application/json");
            }
        };
    }
}