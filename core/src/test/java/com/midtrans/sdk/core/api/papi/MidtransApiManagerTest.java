package com.midtrans.sdk.core.api.papi;

import com.midtrans.sdk.core.Environment;
import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.api.papi.utils.MockMidtransRetrofit;
import com.midtrans.sdk.core.models.papi.CardTokenRequest;
import com.midtrans.sdk.core.models.papi.CardTokenResponse;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;

/**
 * Created by rakawm on 2/13/17.
 */
public class MidtransApiManagerTest {
    MidtransCore midtransCore;
    MidtransApiManager midtransApiManager;
    BehaviorDelegate<MidtransApi> midtransApi;

    @Before
    public void setUp() throws Exception {
        midtransCore = new MidtransCore.Builder()
                .setEnvironment(Environment.SANDBOX)
                .setClientKey("clientkey")
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getCardTokenNormalCardOnSuccess() throws Exception {
        MockRetrofit mockRetrofit = MockMidtransRetrofit.getMidtransRetrofit();
        midtransApi = mockRetrofit.create(MidtransApi.class);
        MidtransApi api = midtransApi.returningResponse(new CardTokenResponse(null, "bni", "200", "token", null));
        CardTokenRequest cardTokenRequest = CardTokenRequest.newNormalCard("cardnumber", "cardcvv", "cardexpirymonth", "cardexpiryyear");
        midtransApiManager = new MidtransApiManager(api);
        midtransApiManager.getCardToken(cardTokenRequest, new MidtransCoreCallback<CardTokenResponse>() {
            @Override
            public void onSuccess(CardTokenResponse object) {
                Assert.assertEquals("200", object.statusCode);
                Assert.assertEquals("token", object.tokenId);
                Assert.assertEquals("bni", object.bank);
            }

            @Override
            public void onFailure(CardTokenResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void getCardTokenSecureCardOnSuccess() throws Exception {
        MockRetrofit mockRetrofit = MockMidtransRetrofit.getMidtransRetrofit();
        midtransApi = mockRetrofit.create(MidtransApi.class);
        MidtransApi api = midtransApi.returningResponse(new CardTokenResponse(null, "bni", "200", "token", "url"));
        CardTokenRequest cardTokenRequest = CardTokenRequest.newNormalSecureCard("cardnumber", "cardcvv", "cardexpirymonth", "cardexpiryyear", true, 10000);
        midtransApiManager = new MidtransApiManager(api);
        midtransApiManager.getCardToken(cardTokenRequest, new MidtransCoreCallback<CardTokenResponse>() {
            @Override
            public void onSuccess(CardTokenResponse object) {
                Assert.assertEquals("200", object.statusCode);
                Assert.assertEquals("token", object.tokenId);
                Assert.assertEquals("bni", object.bank);
            }

            @Override
            public void onFailure(CardTokenResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void getCardTokenTwoClicksCardOnSuccess() throws Exception {
        MockRetrofit mockRetrofit = MockMidtransRetrofit.getMidtransRetrofit();
        midtransApi = mockRetrofit.create(MidtransApi.class);
        MidtransApi api = midtransApi.returningResponse(new CardTokenResponse(null, "bni", "200", "token", "url"));
        CardTokenRequest cardTokenRequest = CardTokenRequest.newNormalTwoClicksCard("saved_token", "cardcvv", true, 10000);
        midtransApiManager = new MidtransApiManager(api);
        midtransApiManager.getCardToken(cardTokenRequest, new MidtransCoreCallback<CardTokenResponse>() {
            @Override
            public void onSuccess(CardTokenResponse object) {
                Assert.assertEquals("200", object.statusCode);
                Assert.assertEquals("token", object.tokenId);
                Assert.assertEquals("bni", object.bank);
            }

            @Override
            public void onFailure(CardTokenResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }
}