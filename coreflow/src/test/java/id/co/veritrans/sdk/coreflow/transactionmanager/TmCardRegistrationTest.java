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

import java.security.cert.CertPathValidatorException;
import java.util.Collections;

import javax.net.ssl.SSLHandshakeException;

import id.co.veritrans.sdk.coreflow.R;
import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.core.SdkCoreFlowBuilder;
import id.co.veritrans.sdk.coreflow.core.TransactionManager;
import id.co.veritrans.sdk.coreflow.core.VeritransRestAPI;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBus;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.models.CardRegistrationResponse;
import id.co.veritrans.sdk.coreflow.APIClientMain;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by ziahaqi on 28/06/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, Logger.class, ConnectivityManager.class, Looper.class, VeritransBusProvider.class})
public class TmCardRegistrationTest extends APIClientMain {
    private TransactionManager transactionManager;
    @Mock
    Context context;
    @Mock
    Resources resources;
    @Mock
    ConnectivityManager connectivityManager;

    @Mock
    RetrofitError mRetrofitError;
    @Mock
    SSLHandshakeException mSslHandshakeException;
    @Mock
    CertPathValidatorException mCertPathValidatorException;

    @Mock
    VeritransRestAPI veritransRestAPIMock;
    @Mock
    RetrofitError retrofitErrorMock;

    @Mock
    BusCollaborator busCollaborator;
    @InjectMocks
    EventBustImplementSample eventBustImplementSample;
    @Mock
    VeritransBus veritransBus;

    VeritransSDK veritransSDK;
    String sampleJsonResponse = "{\"a\":\"a\"}";


    @Captor
    private ArgumentCaptor<Callback<CardRegistrationResponse>> callbackArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> callbackArgumentCaptorCardNumber;
    @Captor
    private ArgumentCaptor<String> callbackArgumentCaptorCardCVV;
    @Captor
    private ArgumentCaptor<String> callbackArgumentCaptorCardYear;
    @Captor
    private ArgumentCaptor<String> callbackArgumentCaptorCardMonth;
    @Captor
    private ArgumentCaptor<String> callbackArgumentCaptorCardKey;

    @Captor
    ArgumentCaptor<VeritransBus> captor = ArgumentCaptor
            .forClass(VeritransBus.class);
    private String mAuthToken = "VT-1dqwd34dwed23e2dw";


    @Before
    public void setup(){
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Looper.class);
        PowerMockito.mockStatic(Logger.class);
        PowerMockito.mock(ConnectivityManager.class);

        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(context.getApplicationContext()).thenReturn(context);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(context.getString(R.string.success_code_200)).thenReturn("200");

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


    @Test
    public void testCardRegistration_whenResponseSuccess() throws Exception {
        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));
        CardRegistrationResponse registrationResponse = new CardRegistrationResponse();

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);

        //registration success
        registrationResponse.setStatusCode("200");
        eventBustImplementSample.cardRegistration(veritransRestAPIMock, CARD_NUMBER, CARD_CVV, CARD_EXP_MONTH,
                CARD_EXP_YEAR, mAuthToken);
        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).registerCard(callbackArgumentCaptorCardNumber.capture(),
                callbackArgumentCaptorCardCVV.capture(),
                callbackArgumentCaptorCardMonth.capture(),
                callbackArgumentCaptorCardYear.capture(),
                callbackArgumentCaptorCardKey.capture(),
                callbackArgumentCaptor.capture());
        callbackArgumentCaptor.getValue().success(registrationResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onCardRegistrationSuccess();


        //retrofitResponse success but transacation not success
        registrationResponse.setStatusCode("212");
        eventBustImplementSample.cardRegistration(veritransRestAPIMock, CARD_NUMBER, CARD_CVV, CARD_EXP_MONTH,
                CARD_EXP_YEAR, mAuthToken);
        Mockito.verify(veritransRestAPIMock, Mockito.times(2)).registerCard(callbackArgumentCaptorCardNumber.capture(),
                callbackArgumentCaptorCardCVV.capture(),
                callbackArgumentCaptorCardMonth.capture(),
                callbackArgumentCaptorCardYear.capture(),
                callbackArgumentCaptorCardKey.capture(),
                callbackArgumentCaptor.capture());
        callbackArgumentCaptor.getValue().success(registrationResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onCardRegistrationFailed();

    }

    @Test
    public void testCardRegistrationFailed_whenResponseError() throws Exception {
        CardRegistrationResponse registrationResponse = new CardRegistrationResponse();
        registrationResponse.setStatusCode("400");
        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);

        eventBustImplementSample.cardRegistration(veritransRestAPIMock, CARD_NUMBER, CARD_CVV, CARD_EXP_MONTH,
                CARD_EXP_YEAR, mAuthToken);
        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).registerCard(callbackArgumentCaptorCardNumber.capture(),
                callbackArgumentCaptorCardCVV.capture(),
                callbackArgumentCaptorCardMonth.capture(),
                callbackArgumentCaptorCardYear.capture(),
                callbackArgumentCaptorCardKey.capture(),
                callbackArgumentCaptor.capture());
        callbackArgumentCaptor.getValue().failure(mRetrofitError);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

        //when certificate invalid
        Mockito.when(mRetrofitError.getCause()).thenReturn(mSslHandshakeException);
        callbackArgumentCaptor.getValue().failure(mRetrofitError);
        Assert.assertEquals(mSslHandshakeException, mRetrofitError.getCause());
    }
}
