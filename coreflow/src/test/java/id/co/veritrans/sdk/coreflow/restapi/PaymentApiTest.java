package id.co.veritrans.sdk.coreflow.restapi;

import android.net.ConnectivityManager;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.concurrent.CountDownLatch;

import id.co.veritrans.sdk.coreflow.SDKConfig;
import id.co.veritrans.sdk.coreflow.core.PaymentAPI;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.models.AuthModel;
import id.co.veritrans.sdk.coreflow.models.CardRegistrationResponse;
import id.co.veritrans.sdk.coreflow.models.RegisterCardResponse;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by ziahaqi on 25/06/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, android.text.TextUtils.class})
public class PaymentApiTest extends APIClientMain {
    private static final String VT_CARD_REG_RESPONSE_SUCCESS = "server_register_card_success.json";
    private AuthModel mAuthModel;
    private String authtoken = "b9a10bcb99a1cede39d1f4f951ab2e16";

    private CardRegistrationResponse mRegisterCardResponse;

    @Before
    public void setup(){
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(android.text.TextUtils.class);

    }

    @Test
    public void VtApiCardRegistrationSuccess() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        PaymentAPI paymentAPI = createVeritransPaymentAPIMock(VT_CARD_REG_RESPONSE_SUCCESS, 200, "success");
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
