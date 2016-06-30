package id.co.veritrans.sdk.coreflow.transactionmanager;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
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
import id.co.veritrans.sdk.coreflow.models.CardTokenRequest;
import id.co.veritrans.sdk.coreflow.models.TokenDetailsResponse;
import id.co.veritrans.sdk.coreflow.APIClientMain;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by ziahaqi on 24/06/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, TextUtils.class, Logger.class, ConnectivityManager.class, Looper.class,
        Base64.class,
        VeritransBusProvider.class})
public class TmGetTokenTest extends APIClientMain{

    private TransactionManager transactionManager;
    @Mock
    Context context;
    @Mock
    Resources resources;
    @Mock
    ConnectivityManager connectivityManager;

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


    @Captor
    private ArgumentCaptor<Callback<TokenDetailsResponse>> callbackArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> cardNumberCaptor;
    @Captor
    private ArgumentCaptor<String> cardCVVCaptor;
    @Captor
    private ArgumentCaptor<String> cardExpMonthCaptor;
    @Captor
    private ArgumentCaptor<String> cardExpYearCaptor;
    @Captor
    private ArgumentCaptor<String> clientKeyCaptor;
    @Captor
    private ArgumentCaptor<String> bankCaptor;
    @Captor
    private ArgumentCaptor<Boolean> instalmentCaptor;
    @Captor
    private ArgumentCaptor<String> instalmentTermCaptor;
    @Captor
    private ArgumentCaptor<Boolean> scureCaptor;
    @Captor
    private ArgumentCaptor<Boolean> twoClickCaptor;
    @Captor
    private ArgumentCaptor<Double> grossAmountCaptor;

    CardTokenRequest cardTokenRequest = new CardTokenRequest();

    @Captor
    ArgumentCaptor<VeritransBus> captor = ArgumentCaptor
            .forClass(VeritransBus.class);
    @Captor
    private ArgumentCaptor<String> tokenIdCaptor;

    @Before
    public void setup(){
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Looper.class);
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(Base64.class);
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

    private CardTokenRequest getCardTokenRequest(){
        cardTokenRequest.setCardNumber(CARD_NUMBER);
        cardTokenRequest.setCardCVV(CARD_CVV);
        cardTokenRequest.setCardExpiryMonth(CARD_EXP_MONTH);
        cardTokenRequest.setCardExpiryYear(CARD_EXP_YEAR);
        cardTokenRequest.setBank("BI");
        cardTokenRequest.setSecure(false);
        cardTokenRequest.setTwoClick(false);
        cardTokenRequest.setGrossAmount(0.0);
        return  cardTokenRequest;
    }


    //get3DSToken

    @Test public void testGetTokenSuccess_get3DSToken_whenResponseNotNull(){
        TokenDetailsResponse tokenDetailsResponse = new TokenDetailsResponse();
        tokenDetailsResponse.setStatusCode("200");



        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).get3DSToken(cardNumberCaptor.capture(),
                cardCVVCaptor.capture(), cardExpMonthCaptor.capture(),
                cardExpYearCaptor.capture(), clientKeyCaptor.capture(), bankCaptor.capture(),
                scureCaptor.capture(), twoClickCaptor.capture(),
                grossAmountCaptor.capture(), callbackArgumentCaptor.capture());

        // when retrofitResponse 20
        callbackArgumentCaptor.getValue().success(tokenDetailsResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenSuccessEvent();

        //when retrofitResponse not 200
        tokenDetailsResponse.setStatusCode("212");
        callbackArgumentCaptor.getValue().success(tokenDetailsResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenFailedEvent();

        tokenDetailsResponse.setStatusMessage("statusnotnull");
        callbackArgumentCaptor.getValue().success(tokenDetailsResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(2)).onGetTokenFailedEvent();
    }


    @Test public void testGetTokenSuccess_get3DSToken_whenresponseNull(){
        TokenDetailsResponse tokenDetailsResponse = null;

        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).get3DSToken(cardNumberCaptor.capture(),
                cardCVVCaptor.capture(), cardExpMonthCaptor.capture(),
                cardExpYearCaptor.capture(), clientKeyCaptor.capture(), bankCaptor.capture(),
                scureCaptor.capture(), twoClickCaptor.capture(),
                grossAmountCaptor.capture(), callbackArgumentCaptor.capture());

        // when retrofitResponse null
        callbackArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }


    @Test
    public void testGetTokenError_get3DSToken(){
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).get3DSToken(cardNumberCaptor.capture(),
                cardCVVCaptor.capture(), cardExpMonthCaptor.capture(),
                cardExpYearCaptor.capture(), clientKeyCaptor.capture(), bankCaptor.capture(),
                scureCaptor.capture(), twoClickCaptor.capture(),
                grossAmountCaptor.capture(), callbackArgumentCaptor.capture());

        //when valid certification
        callbackArgumentCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

        // when invalid certification
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
        Assert.assertNotNull(mSslHandshakeException);
    }


    //getTokenInstalmentOfferTwoClick


    @Test public void testGetTokenSuccess_get3DSTokenInstalmentOffers_whenResponseNotNull(){
        TokenDetailsResponse tokenDetailsResponse = new TokenDetailsResponse();
        tokenDetailsResponse.setStatusCode("200");


        cardTokenRequest.setInstalment(true);


        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).get3DSTokenInstalmentOffers(cardNumberCaptor.capture(),
                cardCVVCaptor.capture(), cardExpMonthCaptor.capture(),
                cardExpYearCaptor.capture(), clientKeyCaptor.capture(), bankCaptor.capture(),
                scureCaptor.capture(), twoClickCaptor.capture(),
                grossAmountCaptor.capture(),
                instalmentCaptor.capture(),
                instalmentTermCaptor.capture(),
                callbackArgumentCaptor.capture());

        // when retrofitResponse 20
        callbackArgumentCaptor.getValue().success(tokenDetailsResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenSuccessEvent();

        //when retrofitResponse not 200
        tokenDetailsResponse.setStatusCode("212");
        callbackArgumentCaptor.getValue().success(tokenDetailsResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenFailedEvent();

        tokenDetailsResponse.setStatusMessage("statusnotnull");
        callbackArgumentCaptor.getValue().success(tokenDetailsResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(2)).onGetTokenFailedEvent();
    }



    @Test public void testGetTokenSuccess_get3DSTokenInstalmentOffers_whenresponseNull(){
        TokenDetailsResponse tokenDetailsResponse = null;

        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

        cardTokenRequest.setInstalment(true);


        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).get3DSTokenInstalmentOffers(cardNumberCaptor.capture(),
                cardCVVCaptor.capture(), cardExpMonthCaptor.capture(),
                cardExpYearCaptor.capture(), clientKeyCaptor.capture(), bankCaptor.capture(),
                scureCaptor.capture(), twoClickCaptor.capture(),
                grossAmountCaptor.capture(),
                instalmentCaptor.capture(),
                instalmentTermCaptor.capture(),
                callbackArgumentCaptor.capture());

        // when retrofitResponse null
        callbackArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }


    @Test
    public void testGetTokenError_get3DSTokenInstalmentOffers(){
        cardTokenRequest.setInstalment(true);
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).get3DSTokenInstalmentOffers(cardNumberCaptor.capture(),
                cardCVVCaptor.capture(), cardExpMonthCaptor.capture(),
                cardExpYearCaptor.capture(), clientKeyCaptor.capture(), bankCaptor.capture(),
                scureCaptor.capture(), twoClickCaptor.capture(),
                grossAmountCaptor.capture(),
                instalmentCaptor.capture(),
                instalmentTermCaptor.capture(),
                callbackArgumentCaptor.capture());

        //when valid certification
        callbackArgumentCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

        // when invalid certification
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
        Assert.assertNotNull(mSslHandshakeException);
    }

    //getTokenTwoClick
    @Test public void testGetTokenSuccess_getTokenTwoClick_whenResponseNotNull(){
        TokenDetailsResponse tokenDetailsResponse = new TokenDetailsResponse();
        tokenDetailsResponse.setStatusCode("200");

        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

        cardTokenRequest.setInstalment(false);
        cardTokenRequest.setTwoClick(true);


        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).getTokenTwoClick(
                cardCVVCaptor.capture(),
                tokenIdCaptor.capture(),
                twoClickCaptor.capture(),
                scureCaptor.capture(),
                grossAmountCaptor.capture(),
                bankCaptor.capture(),
                clientKeyCaptor.capture(),
                callbackArgumentCaptor.capture());

        // when retrofitResponse 20
        callbackArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenSuccessEvent();

        //when retrofitResponse not 200
        tokenDetailsResponse.setStatusCode("212");
        callbackArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenFailedEvent();

        tokenDetailsResponse.setStatusMessage("statusnotnull");
        callbackArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(2)).onGetTokenFailedEvent();
    }



    @Test public void testGetTokenSuccess_getTokenTwoClick_whenresponseNull(){
        TokenDetailsResponse tokenDetailsResponse = null;

        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

        cardTokenRequest.setInstalment(false);
        cardTokenRequest.setTwoClick(true);


        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).getTokenTwoClick(
                cardCVVCaptor.capture(),
                tokenIdCaptor.capture(),
                twoClickCaptor.capture(),
                scureCaptor.capture(),
                grossAmountCaptor.capture(),
                bankCaptor.capture(),
                clientKeyCaptor.capture(),
                callbackArgumentCaptor.capture());

        // when retrofitResponse null
        callbackArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }


    @Test
    public void testGetTokenError_getTokenTwoClick(){
        cardTokenRequest.setInstalment(false);
        cardTokenRequest.setTwoClick(true);
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);


        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).getTokenTwoClick(
                cardCVVCaptor.capture(),
                tokenIdCaptor.capture(),
                twoClickCaptor.capture(),
                scureCaptor.capture(),
                grossAmountCaptor.capture(),
                bankCaptor.capture(),
                clientKeyCaptor.capture(),
                callbackArgumentCaptor.capture());

        //when valid certification
        callbackArgumentCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

        // when invalid certification
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
        Assert.assertNotNull(mSslHandshakeException);
    }


    //getTokenInstalmentOfferTwoClick
    @Test public void testGetTokenSuccess_getTokenInstalmentOfferTwoClick_whenResponseNotNull(){
        TokenDetailsResponse tokenDetailsResponse = new TokenDetailsResponse();
        tokenDetailsResponse.setStatusCode("200");

        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

        cardTokenRequest.setInstalment(true);
        cardTokenRequest.setTwoClick(true);


        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).getTokenInstalmentOfferTwoClick(
                cardCVVCaptor.capture(),
                tokenIdCaptor.capture(),
                twoClickCaptor.capture(),
                scureCaptor.capture(),
                grossAmountCaptor.capture(),
                bankCaptor.capture(),
                clientKeyCaptor.capture(),
                instalmentCaptor.capture(),
                instalmentTermCaptor.capture(),
                callbackArgumentCaptor.capture());

        // when retrofitResponse 20
        callbackArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenSuccessEvent();

        //when retrofitResponse not 200
        tokenDetailsResponse.setStatusCode("212");
        callbackArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenFailedEvent();

        tokenDetailsResponse.setStatusMessage("statusnotnull");
        callbackArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(2)).onGetTokenFailedEvent();
    }



    @Test public void testGetTokenSuccess_getTokenInstalmentOfferTwoClick_whenresponseNull(){
        TokenDetailsResponse tokenDetailsResponse = null;

        Response response = new Response("URL", 200, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

        cardTokenRequest.setInstalment(true);
        cardTokenRequest.setTwoClick(true);


        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).getTokenInstalmentOfferTwoClick(
                cardCVVCaptor.capture(),
                tokenIdCaptor.capture(),
                twoClickCaptor.capture(),
                scureCaptor.capture(),
                grossAmountCaptor.capture(),
                bankCaptor.capture(),
                clientKeyCaptor.capture(),
                instalmentCaptor.capture(),
                instalmentTermCaptor.capture(),
                callbackArgumentCaptor.capture());

        // when retrofitResponse null
        callbackArgumentCaptor.getValue().success(tokenDetailsResponse, response);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }


    @Test
    public void testGetTokenError_getTokenInstalmentOfferTwoClick(){
        cardTokenRequest.setInstalment(true);
        cardTokenRequest.setTwoClick(true);


        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getToken(veritransRestAPIMock,cardTokenRequest);

        Mockito.verify(veritransRestAPIMock, Mockito.times(1)).getTokenInstalmentOfferTwoClick(
                cardCVVCaptor.capture(),
                tokenIdCaptor.capture(),
                twoClickCaptor.capture(),
                scureCaptor.capture(),
                grossAmountCaptor.capture(),
                bankCaptor.capture(),
                clientKeyCaptor.capture(),
                instalmentCaptor.capture(),
                instalmentTermCaptor.capture(),
                callbackArgumentCaptor.capture());

        //when valid certification
        callbackArgumentCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

        // when invalid certification
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
        Assert.assertNotNull(mSslHandshakeException);
    }
}
