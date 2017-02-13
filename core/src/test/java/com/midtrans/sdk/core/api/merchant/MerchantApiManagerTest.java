package com.midtrans.sdk.core.api.merchant;

import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenRequest;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenResponse;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rakawm on 2/13/17.
 */
public class MerchantApiManagerTest {
    MerchantApi merchantApi;
    Call<CheckoutTokenResponse> successCallback;
    Call<CheckoutTokenResponse> failureCallback;
    Call<CheckoutTokenResponse> errorCallback;
    CheckoutTokenResponse checkoutTokenResponse;

    @Before
    public void setUp() throws Exception {
        checkoutTokenResponse = new CheckoutTokenResponse("token", null);
        merchantApi = Mockito.mock(MerchantApi.class);
        successCallback = new Call<CheckoutTokenResponse>() {
            @Override
            public Response<CheckoutTokenResponse> execute() throws IOException {
                return Response.success(checkoutTokenResponse);
            }

            @Override
            public void enqueue(Callback<CheckoutTokenResponse> callback) {
            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<CheckoutTokenResponse> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };

        failureCallback = new Call<CheckoutTokenResponse>() {
            @Override
            public Response<CheckoutTokenResponse> execute() throws IOException {
                List<String> errors = new ArrayList<>();
                errors.add("Error message");
                return Response.success(new CheckoutTokenResponse(null, errors));
            }

            @Override
            public void enqueue(Callback<CheckoutTokenResponse> callback) {
            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<CheckoutTokenResponse> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };

        errorCallback = new Call<CheckoutTokenResponse>() {
            @Override
            public Response<CheckoutTokenResponse> execute() throws IOException {
                return Response.error(400, new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return MediaType.parse("application/json");
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                });
            }

            @Override
            public void enqueue(Callback<CheckoutTokenResponse> callback) {
            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<CheckoutTokenResponse> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void checkoutOnSuccess() throws Exception {
        final CheckoutTokenRequest checkoutTokenRequest = CheckoutTokenRequest.newMinimalCheckout("order_id", 10000);
        Mockito.doReturn(successCallback).when(merchantApi).checkout("url", checkoutTokenRequest);
        MerchantApiManager merchantApiManager = new MerchantApiManager(merchantApi);
        merchantApiManager.checkout("url", checkoutTokenRequest, new MidtransCoreCallback<CheckoutTokenResponse>() {
            @Override
            public void onSuccess(CheckoutTokenResponse object) {
                Assert.assertNotNull(checkoutTokenResponse.token);
            }

            @Override
            public void onFailure(CheckoutTokenResponse object) {
                // Do Nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do Nothing
            }
        });
    }

    @Test
    public void checkoutOnFailure() throws Exception {
        final CheckoutTokenRequest checkoutTokenRequest = CheckoutTokenRequest.newMinimalCheckout("order_id", 10000);
        Mockito.doReturn(failureCallback).when(merchantApi).checkout("url", checkoutTokenRequest);
        MerchantApiManager merchantApiManager = new MerchantApiManager(merchantApi);
        merchantApiManager.checkout("url", checkoutTokenRequest, new MidtransCoreCallback<CheckoutTokenResponse>() {
            @Override
            public void onSuccess(CheckoutTokenResponse object) {
                // Do nothing
            }

            @Override
            public void onFailure(CheckoutTokenResponse object) {
                Assert.assertNull(object.token);
                Assert.assertNotNull(object.errorMessages);
                Assert.assertEquals(1, object.errorMessages.size());
            }

            @Override
            public void onError(Throwable throwable) {
                // Do Nothing
            }
        });
    }

    @Test
    public void checkoutOnError() throws Exception {
        final CheckoutTokenRequest checkoutTokenRequest = CheckoutTokenRequest.newMinimalCheckout("order_id", 10000);
        Mockito.doReturn(errorCallback).when(merchantApi).checkout("url", checkoutTokenRequest);
        MerchantApiManager merchantApiManager = new MerchantApiManager(merchantApi);
        merchantApiManager.checkout("url", checkoutTokenRequest, new MidtransCoreCallback<CheckoutTokenResponse>() {
            @Override
            public void onSuccess(CheckoutTokenResponse object) {
                // Do nothing
            }

            @Override
            public void onFailure(CheckoutTokenResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                Assert.assertNotNull(throwable);
            }
        });
    }
}