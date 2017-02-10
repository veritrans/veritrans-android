package com.midtrans.sdk.core.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by rakawm on 10/19/16.
 */

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request headerInterceptedRequest = request.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("X-Source", "mobile")
                .build();

        return chain.proceed(headerInterceptedRequest);
    }
}
