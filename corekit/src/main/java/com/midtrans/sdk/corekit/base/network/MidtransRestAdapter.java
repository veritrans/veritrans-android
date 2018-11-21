package com.midtrans.sdk.corekit.base.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import android.os.Build;

import com.midtrans.sdk.corekit.core.merchant.MerchantApiService;
import com.midtrans.sdk.corekit.core.snap.SnapApiManager;
import com.midtrans.sdk.corekit.core.snap.SnapApiService;
import com.midtrans.sdk.corekit.utilities.Logger;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MidtransRestAdapter {

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

    public static SnapApiService newSnapApiService(String merchantBaseUrl, int timeout) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(merchantBaseUrl)
                .client(newOkHttpClient(timeout))
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
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
//                .registerTypeAdapter(CreditCardPaymentParams.class, new CustomTypeAdapter())
                .create();
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

    private static OkHttpClient.Builder delegateTlsCompat(OkHttpClient.Builder builder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            try {

                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                builder.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()), getTrustManager());

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                builder.connectionSpecs(specs);
            } catch (Exception exc) {
                Logger.error("OkHttpTLSCompat Error while setting TLS 1.2", exc);
            }
        }

        return builder;
    }

    private static X509TrustManager getTrustManager() throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }

        return (X509TrustManager) trustManagers[0];
    }

    private static OkHttpClient newOkHttpClient(int timeout) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        delegateTlsCompat(builder);
        return builder
                .addInterceptor(newHttpLoggingInterceptor())
                .addInterceptor(newHeaderInterceptor())
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .build();
    }

}