package com.midtrans.sdk.corekit.restapi;

import com.midtrans.sdk.corekit.models.AuthModel;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ziahaqi on 24/06/2016.
 */
public class DummyPaymentApiGetAuthTokenCallback implements Callback<AuthModel> {

    private AuthModel authModel;
    private Response response;
    private RetrofitError error;
    private String message = "null";

    @Override
    public void success(AuthModel authModel, Response response) {
        message = "success";
        this.authModel = authModel;
        this.response = response;
        System.out.println("On success");
    }

    @Override
    public void failure(RetrofitError error) {
        this.error = error;
        this.message = "failure";
    }

    public AuthModel getAuthModel() {
        return authModel;
    }

    public Response getResponse() {
        return response;
    }

    public RetrofitError getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
