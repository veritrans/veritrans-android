package com.midtrans.sdk.corekit.restapi;

import android.util.Log;

import com.midtrans.sdk.corekit.APIClientMain;
import com.midtrans.sdk.corekit.SDKConfigTest;
import com.midtrans.sdk.corekit.core.MidtransRestAPI;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.CountDownLatch;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by ziahaqi on 25/06/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, android.text.TextUtils.class})
public class PaymentApiTest extends APIClientMain {

    private CardRegistrationResponse mRegisterCardResponse;

    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(android.text.TextUtils.class);
    }

    @Test
    public void vt_ApiCardRegistrationSuccess() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        MidtransRestAPI paymentAPI = createVeritransPaymentAPIMock(VT_CARD_REG_RESPONSE_SUCCESS, 200, "success");
        paymentAPI.registerCard(APIClientMain.CARD_NUMBER,
                APIClientMain.CARD_CVV,
                APIClientMain.CARD_EXP_MONTH,
                APIClientMain.CARD_EXP_YEAR,
                SDKConfigTest.CLIENT_KEY, new Callback<CardRegistrationResponse>() {
                    @Override
                    public void success(CardRegistrationResponse cardRegistrationResponse, Response response) {
                        mRegisterCardResponse = cardRegistrationResponse;
                        latch.countDown();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        latch.countDown();
                    }
                });

        latch.await();
        assertNotNull(mRegisterCardResponse);
        assertEquals("200", mRegisterCardResponse.getStatusCode());
    }

}
