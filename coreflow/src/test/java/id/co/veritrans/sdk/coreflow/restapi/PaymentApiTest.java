package id.co.veritrans.sdk.coreflow.restapi;

import android.util.Log;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.CountDownLatch;

import id.co.veritrans.sdk.coreflow.SDKConfig;
import id.co.veritrans.sdk.coreflow.core.PaymentAPI;
import id.co.veritrans.sdk.coreflow.core.VeritransRestAPI;
import id.co.veritrans.sdk.coreflow.models.AuthModel;
import id.co.veritrans.sdk.coreflow.models.CardRegistrationResponse;
import id.co.veritrans.sdk.coreflow.models.TokenDetailsResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ziahaqi on 25/06/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, android.text.TextUtils.class})
public class PaymentApiTest extends APIClientMain {

    private CardRegistrationResponse mRegisterCardResponse;

    @Before
    public void setup(){
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(android.text.TextUtils.class);
    }

    @Test
    public void vt_ApiCardRegistrationSuccess() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        VeritransRestAPI paymentAPI = createVeritransPaymentAPIMock(VT_CARD_REG_RESPONSE_SUCCESS, 200, "success");
        paymentAPI.registerCard(APIClientMain.CARD_NUMBER,
                APIClientMain.CARD_CVV,
                APIClientMain.CARD_EXP_MONTH,
                APIClientMain.CARD_EXP_YEAR,
                SDKConfig.CLIENT_KEY, new Callback<CardRegistrationResponse>() {
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
        assertEquals("200",mRegisterCardResponse.getStatusCode());
    }


    public void merchantApiCardRegistrationSuccess() throws Exception {

    }
}
