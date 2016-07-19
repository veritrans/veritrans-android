package id.co.veritrans.sdk.coreflow.core;

import android.content.Context;
import android.content.res.Resources;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.cert.CertPathValidatorException;
import java.util.Collections;

import javax.net.ssl.SSLHandshakeException;

import id.co.veritrans.sdk.coreflow.R;
import id.co.veritrans.sdk.coreflow.SDKConfigTest;
import id.co.veritrans.sdk.coreflow.analytics.MixpanelApi;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBus;
import id.co.veritrans.sdk.coreflow.models.snap.Token;
import id.co.veritrans.sdk.coreflow.models.snap.Transaction;
import id.co.veritrans.sdk.coreflow.transactionmanager.BusCollaborator;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by ziahaqi on 7/18/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, TextUtils.class, Logger.class, Looper.class, Base64.class})
@PowerMockIgnore("javax.net.ssl.*")
public class SnapTransactionManagerTest {

    protected String sampleJsonResponse = "{\"response\":\"response\"}";


    protected Response retrofitResponse = new Response("URL", 200, "success", Collections.EMPTY_LIST,
            new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

    @Mock
    private Context contextMock;
    @Mock
    private Resources resourcesMock;
    private VeritransSDK veritransSDK;
    @Mock
    private MixpanelAnalyticsManager mixpanelAnalyticsManagerMock;
    @Mock
    private MixpanelApi mixpanelApiMock;
    private SnapTransactionManager transactionManager;


    @Mock
    protected BusCollaborator busCollaborator;

    @InjectMocks
    protected VeritransAndroidSDKTest.EventBustImplementSample eventBusImplementSample;
    @Mock
    private VeritransBus veritransBusMock;
    @Captor
    private ArgumentCaptor<Callback<Token>> callbackSnapTokenResponseCaptor;

    @Mock
    private Token snapTokenMock;
    private String tokenId = "aa3afad7-a346-4db6-9cb3-737f24e4fc56";
    @Mock
    private MerchantRestAPI merchantApi;
    @Mock
    private RetrofitError retrofitError;
    @Mock
    private CertPathValidatorException errorInvalidCertPatMock;
    @Mock
    private SSLHandshakeException errorInvalidSSLException;
    @Mock
    private SnapRestAPI snapAPI;
    @Captor
    private ArgumentCaptor<Callback<Transaction>> transactionCaptorMock;
    @Mock
    private Transaction transactionResponseMock;
    @Mock
    private java.lang.Throwable errorGeneralMock;
    @Captor
    private ArgumentCaptor<String> tokenIdCaptor;

    @Before
    public void setup(){
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Looper.class);
        PowerMockito.mockStatic(Base64.class);
        PowerMockito.mockStatic(Logger.class);

        Mockito.when(contextMock.getResources()).thenReturn(resourcesMock);
        Mockito.when(contextMock.getApplicationContext()).thenReturn(contextMock);
        Mockito.when(contextMock.getString(R.string.success_code_200)).thenReturn("200");
        Mockito.when(contextMock.getString(R.string.success_code_201)).thenReturn("201");
        Mockito.when(contextMock.getString(R.string.success)).thenReturn("success");

        veritransSDK = new SdkCoreFlowBuilder(contextMock, SDKConfigTest.CLIENT_KEY, SDKConfigTest.MERCHANT_BASE_URL)
                .enableLog(true)
                .setDefaultText("open_sans_regular.ttf")
                .setSemiBoldText("open_sans_semibold.ttf")
                .setBoldText("open_sans_bold.ttf")
                .setMerchantName("Veritrans Example Merchant")
                .buildSDK();

        mixpanelAnalyticsManagerMock.setMixpanelApi(mixpanelApiMock);
        transactionManager = veritransSDK.getmSnapTransactionManager();
        transactionManager.setAnalyticsManager(mixpanelAnalyticsManagerMock);
        eventBusImplementSample.registerBus(veritransBusMock);

    }

    /*
     * get snaptoken test
     */
    @Test
    public void getTokenTestSuccess(){
        Mockito.when(snapTokenMock.getTokenId()).thenReturn(tokenId);
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.getSnapToken(merchantApi);

        Mockito.verify(merchantApi, Mockito.times(1)).getSnapToken(callbackSnapTokenResponseCaptor.capture());

        callbackSnapTokenResponseCaptor.getValue().success(snapTokenMock, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenSuccessEvent();
    }

    @Test
    public void getTokenTestSuccess_responseCodeNon200(){
        retrofitResponse = new Response("URL", 300, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));
        Mockito.when(snapTokenMock.getTokenId()).thenReturn(tokenId);
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.getSnapToken(merchantApi);

        Mockito.verify(merchantApi, Mockito.times(1)).getSnapToken(callbackSnapTokenResponseCaptor.capture());

        callbackSnapTokenResponseCaptor.getValue().success(snapTokenMock, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenFailedEvent();
    }


    @Test
    public void getTokenTestSuccess_whenResponseNull(){
        Mockito.when(snapTokenMock.getTokenId()).thenReturn(tokenId);
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.getSnapToken(merchantApi);

        Mockito.verify(merchantApi, Mockito.times(1)).getSnapToken(callbackSnapTokenResponseCaptor.capture());

        callbackSnapTokenResponseCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }

    @Test
    public void getTokenTestError_whenValidSSL(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.getSnapToken(merchantApi);

        Mockito.verify(merchantApi, Mockito.times(1)).getSnapToken(callbackSnapTokenResponseCaptor.capture());

        callbackSnapTokenResponseCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();
    }

    @Test
    public void getTokenTestError_whenInvalidSSL(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.getSnapToken(merchantApi);

        Mockito.verify(merchantApi, Mockito.times(1)).getSnapToken(callbackSnapTokenResponseCaptor.capture());

        callbackSnapTokenResponseCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();
    }

    @Test
    public void getTokenTestError(){
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.getSnapToken(merchantApi);

        Mockito.verify(merchantApi, Mockito.times(1)).getSnapToken(callbackSnapTokenResponseCaptor.capture());
        callbackSnapTokenResponseCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }


    /*
     * get paymentType
     */

    @Test
    public void getSnapToken_whenTokenNull(){
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.getPaymentType(snapAPI, null);

        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void getSnapTokenSuccess_whenResponseNull(){
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.getPaymentType(snapAPI, tokenId);

        Mockito.verify(snapAPI).getSnapTransaction(tokenIdCaptor.capture(), transactionCaptorMock.capture());
        transactionCaptorMock.getValue().success(null, retrofitResponse);

        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    //!untest
    @Test
    public void getSnapTransactionSuccess_whenCode200(){
        System.out.println("test:" +        retrofitResponse.getStatus()
        );
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.getPaymentType(snapAPI, tokenId);

        Mockito.verify(snapAPI).getSnapTransaction(tokenIdCaptor.capture(), transactionCaptorMock.capture());
        transactionCaptorMock.getValue().success(transactionResponseMock, retrofitResponse);

        Mockito.verify(busCollaborator).onGetPaymentListSuccessEvent();
    }

    @Test
    public void getSnapTransactionSuccess_whenCodeNot200(){

        retrofitResponse = new Response("URL", 300, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.getPaymentType(snapAPI, tokenId);

        Mockito.verify(snapAPI).getSnapTransaction(tokenIdCaptor.capture(), transactionCaptorMock.capture());
        transactionCaptorMock.getValue().success(transactionResponseMock, retrofitResponse);

        Mockito.verify(busCollaborator).onGetPaymentListFailedEvent();
    }


    @Test
    public void getSnapTokenError_whenCertPathInvalid(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.getPaymentType(snapAPI, tokenId);

        Mockito.verify(snapAPI).getSnapTransaction(tokenIdCaptor.capture(), transactionCaptorMock.capture());
        transactionCaptorMock.getValue().failure(retrofitError);

        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void getSnapTokenError_whenInvalidSSL(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.getPaymentType(snapAPI, tokenId);
        Mockito.verify(snapAPI).getSnapTransaction(tokenIdCaptor.capture(), transactionCaptorMock.capture());
        transactionCaptorMock.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void getSnapTokenError_whenGeneralError(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.getPaymentType(snapAPI, tokenId);
        Mockito.verify(snapAPI).getSnapTransaction(tokenIdCaptor.capture(), transactionCaptorMock.capture());
        transactionCaptorMock.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }
}
