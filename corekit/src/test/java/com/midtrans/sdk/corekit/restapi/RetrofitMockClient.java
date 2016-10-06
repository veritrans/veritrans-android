package com.midtrans.sdk.corekit.restapi;

import java.io.IOException;
import java.util.Collections;

import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by ziahaqi on 23/06/2016.
 */
public class RetrofitMockClient implements Client {
    private static final String MIME_TYPE = "application/json";
    private String jsonResponse;
    private int statusCode = 200;
    private String reason;

    public RetrofitMockClient(int statusCode, String reason, String jsonResponse) {
        this.statusCode = statusCode;
        this.reason = reason;
        this.jsonResponse = jsonResponse;
    }

    @Override
    public Response execute(Request request) throws IOException {
        return createDummyJsonResponse(request.getUrl(), statusCode, reason, jsonResponse);
    }

    private Response createDummyJsonResponse(String url, int responseCode, String reason, String json) {
        return new Response(url, responseCode, reason, Collections.EMPTY_LIST,
                new TypedByteArray(MIME_TYPE, json.getBytes()));
    }
}
