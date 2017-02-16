package com.midtrans.sdk.core.api.papi.utils;

import com.midtrans.sdk.core.utils.Utilities;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * Created by rakawm on 2/14/17.
 */

public class MockMidtransRetrofit {

    public static MockRetrofit getMidtransRetrofit() {
        return new MockRetrofit.Builder(provideMerchantRetrofit())
                .build();
    }

    public static MockRetrofit getErrorMidtransRetrofit() {
        NetworkBehavior networkBehavior = NetworkBehavior.create();
        networkBehavior.setFailureException(new RuntimeException("error"));

        return new MockRetrofit.Builder(provideMerchantRetrofit())
                .networkBehavior(networkBehavior)
                .build();
    }

    private static Retrofit provideMerchantRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.midtrans.com/v2/")
                .addConverterFactory(GsonConverterFactory.create(Utilities.buildGson()))
                .client(new OkHttpClient())
                .build();
    }
}
