package id.co.veritrans.sdk.coreflow.transactionmanager;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Looper;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import id.co.veritrans.sdk.coreflow.core.PaymentAPI;
import id.co.veritrans.sdk.coreflow.core.SdkCoreFlowBuilder;
import id.co.veritrans.sdk.coreflow.core.TransactionManager;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBus;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.events.CardRegistrationSuccessEvent;
import id.co.veritrans.sdk.coreflow.restapi.APIClientMain;

/**
 * Created by ziahaqi on 24/06/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, ConnectivityManager.class, Looper.class, VeritransBusProvider.class})
public class TransactionManagerTest extends APIClientMain{
    private static final String CARD_REGISTER_SUCCESS = "server_register_card_success.json";
    private static final String CARD_NUMBER = "4811111111111114";
    private static final String CARD_CVV = "123";
    private static final String CARD_EXP_MONTH = "01";
    private static final String CARD_EXP_YEAR = "20";
    private TransactionManager transactionManager;
    @Mock
    Context context;
    @Mock
    Resources resources;
    @Mock
    ConnectivityManager connectivityManager;

    @Mock
    SampleClass sampleClass;
    @InjectMocks
    EventBustImplementSample eventBustImplementSample;
    @Mock
    VeritransBus veritransBus;


    VeritransSDK veritransSDK;



    @Captor
    private ArgumentCaptor<CardRegistrationSuccessEvent> cardRegistrationSuccessEventArgumentCaptor;

    @Captor
    ArgumentCaptor<VeritransBus> captor = ArgumentCaptor
            .forClass(VeritransBus.class);

    @Before
    public void setup(){
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Looper.class);
        PowerMockito.mock(ConnectivityManager.class);

        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(context.getApplicationContext()).thenReturn(context);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
//        Mockito.when(VeritransBusProvider.getInstance()).thenReturn(veritransBus);
//
        veritransSDK = new SdkCoreFlowBuilder(context, "SDK", "hi")
                .enableLog(true)
                .setDefaultText("open_sans_regular.ttf")
                .setSemiBoldText("open_sans_semibold.ttf")
                .setBoldText("open_sans_bold.ttf")
                .setMerchantName("Veritrans Example Merchant")
                .buildSDK();
        transactionManager = veritransSDK.getVeritransSDK().getTransactionManager();
    }

    @Test
    public void testt(){
        Assert.assertNotNull(context);
        Assert.assertNotNull(eventBustImplementSample);

    }



//    private PaymentAPI createVeritransPaymentAPIMock(String jsonResponseName, int responseCode, String reason) throws Exception {
//
//        RetrofitMockClient client = RestAPIMocUtilites.getClient(this.getClass().getClassLoader(),
//                200, "authorized", "merchant_auth_ok.json");
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setEndpoint(BuildConfig.BASE_URL)
//                .setClient(client)
//
//                .setConverter(new GsonConverter(new Gson()))
//                .build();
//        return restAdapter.create(PaymentAPI.class);
//    }

    public void testCardRegistration() throws Exception {
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        PaymentAPI paymentAPI = createVeritransPaymentAPIMock(CARD_REGISTER_SUCCESS,
                200, "registered");
        eventBustImplementSample.regCard(paymentAPI, CARD_NUMBER,CARD_CVV, CARD_EXP_MONTH, CARD_EXP_YEAR);
//        VeritransBusProvider.getInstance().post(new CardRegistrationSuccessEvent(new CardRegistrationResponse(),"sdf"));

//        Mockito.verify(sampleClass, Mockito.times(1)).callMethod(event);
        Mockito.verify(sampleClass).callMethod(cardRegistrationSuccessEventArgumentCaptor.capture());

        Assert.assertNotNull(paymentAPI);
        Assert.assertEquals("200", cardRegistrationSuccessEventArgumentCaptor.getValue().getResponse().getStatusCode());


        Assert.assertEquals(1,1);
//        Assert.assertEquals("200", cardRegistrationSuccessEventArgumentCaptor.getValue().getResponse().getStatusCode());

    }


}
