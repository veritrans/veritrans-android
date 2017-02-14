package com.midtrans.sdk.core.api.merchant.utils;

import com.midtrans.sdk.core.utils.Utilities;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * Created by rakawm on 2/14/17.
 */
public class MockMerchantRetrofit {
    public static MockRetrofit getMockMerchantRetrofit() {
        return new MockRetrofit.Builder(provideMerchantRetrofit())
                .networkBehavior(NetworkBehavior.create())
                .build();
    }

    public static MockRetrofit getErrorMerchantRetrofit() {
        NetworkBehavior networkBehavior = NetworkBehavior.create();
        networkBehavior.setFailureException(new RuntimeException("error"));
        return new MockRetrofit.Builder(provideMerchantRetrofit())
                .networkBehavior(networkBehavior)
                .build();
    }

    private static Retrofit provideMerchantRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://rakawm-snap.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(Utilities.buildGson()))
                .client(new OkHttpClient())
                .build();
    }
}
