package com.midtrans.sdk.core.api.merchant;

import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.api.merchant.utils.MockMerchantRetrofit;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenRequest;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenResponse;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;

/**
 * Created by rakawm on 2/13/17.
 */
public class MerchantApiManagerTest {
    BehaviorDelegate<MerchantApi> merchantApi;
    CheckoutTokenResponse checkoutTokenResponse;

    @Test
    public void checkoutOnSuccess() throws Exception {
        checkoutTokenResponse = new CheckoutTokenResponse("token_response", null);
        final CheckoutTokenRequest checkoutTokenRequest = CheckoutTokenRequest.newMinimalCheckout("order_id", 10000);
        MockRetrofit mockRetrofit = MockMerchantRetrofit.getMockMerchantRetrofit();
        merchantApi = mockRetrofit.create(MerchantApi.class);
        MerchantApi api = merchantApi.returningResponse(checkoutTokenResponse);
        MerchantApiManager merchantApiManager = new MerchantApiManager(api);
        merchantApiManager.checkout("url", checkoutTokenRequest, new MidtransCoreCallback<CheckoutTokenResponse>() {
            @Override
            public void onSuccess(CheckoutTokenResponse object) {
                Assert.assertNotNull(object.token);
                Assert.assertEquals("token_response", object.token);
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
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add("error_messages");
        checkoutTokenResponse = new CheckoutTokenResponse(null, errorMessages);
        final CheckoutTokenRequest checkoutTokenRequest = CheckoutTokenRequest.newMinimalCheckout("order_id", 10000);
        MockRetrofit mockRetrofit = MockMerchantRetrofit.getMockMerchantRetrofit();
        merchantApi = mockRetrofit.create(MerchantApi.class);
        MerchantApi api = merchantApi.returningResponse(checkoutTokenResponse);
        MerchantApiManager merchantApiManager = new MerchantApiManager(api);
        merchantApiManager.checkout("url", checkoutTokenRequest, new MidtransCoreCallback<CheckoutTokenResponse>() {
            @Override
            public void onSuccess(CheckoutTokenResponse object) {
                // Do Nothing
            }

            @Override
            public void onFailure(CheckoutTokenResponse object) {
                Assert.assertNotNull(object.errorMessages);
                Assert.assertEquals(1, object.errorMessages.size());
                Assert.assertEquals("error_messages", object.errorMessages.get(0));
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
        MockRetrofit mockRetrofit = MockMerchantRetrofit.getErrorMerchantRetrofit();
        merchantApi = mockRetrofit.create(MerchantApi.class);
        MerchantApi api = merchantApi.returningResponse(null);
        MerchantApiManager merchantApiManager = new MerchantApiManager(api);
        merchantApiManager.checkout("url", checkoutTokenRequest, new MidtransCoreCallback<CheckoutTokenResponse>() {
            @Override
            public void onSuccess(CheckoutTokenResponse object) {
                // Do Nothing
            }

            @Override
            public void onFailure(CheckoutTokenResponse object) {
                // Do Nothing
            }

            @Override
            public void onError(Throwable throwable) {
                Assert.assertNotNull(throwable);
            }
        });
    }
}