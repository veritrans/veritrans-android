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

import javax.net.ssl.SSLHandshakeException;

import id.co.veritrans.sdk.coreflow.R;
import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.core.MerchantRestAPI;
import id.co.veritrans.sdk.coreflow.core.SdkCoreFlowBuilder;
import id.co.veritrans.sdk.coreflow.core.TransactionManager;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBus;
import id.co.veritrans.sdk.coreflow.models.CardResponse;
import id.co.veritrans.sdk.coreflow.APIClientMain;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by ziahaqi on 29/06/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, Logger.class, TextUtils.class, Looper.class, Base64.class})
public class TmGetCardsTest extends APIClientMain {
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
    MerchantRestAPI merchantRestAPIMock;
    @Mock
    RetrofitError retrofitErrorMock;

    @Mock
    BusCollaborator busCollaborator;

    @InjectMocks
    EventBustImplementSample eventBustImplementSample;
    @Mock
    VeritransBus veritransBus;

    VeritransSDK veritransSDK;
    private String mToken = "VT-423wedwe4324r34";
    @Captor
    private ArgumentCaptor<String> xauthCaptor;
    @Captor
    private ArgumentCaptor<Callback<CardResponse>> responseCallbackCaptor;
    @Captor
    private ArgumentCaptor<String> savedTokenIdCaptor;

    @Before
    public void setup(){
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(Logger.class);
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Looper.class);
        PowerMockito.mockStatic(Base64.class);

        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(context.getApplicationContext()).thenReturn(context);
        Mockito.when(context.getString(R.string.success_code_200)).thenReturn("200");
        Mockito.when(context.getString(R.string.success_code_201)).thenReturn("201");

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
    public void testPaymentGetCardsCardSuccess_whenResponseNotNull() throws Exception {

        CardResponse deleteCardResponse = new CardResponse();
        deleteCardResponse.setCode(200);
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getCards(merchantRestAPIMock, mToken);

        Mockito.verify(merchantRestAPIMock).getCard(xauthCaptor.capture(),  responseCallbackCaptor.capture());

        //response code 200 /201
        responseCallbackCaptor.getValue().success(deleteCardResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetCardSuccess();

        //response not code 200 /201
        deleteCardResponse.setCode(300);
        Mockito.verify(merchantRestAPIMock).getCard(xauthCaptor.capture(), responseCallbackCaptor.capture());
        responseCallbackCaptor.getValue().success(deleteCardResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetCardFailed();
    }


    @Test
    public void testPaymentGetCardsCardSuccess_whenResponseNull() throws Exception {

        CardResponse cardResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getCards(merchantRestAPIMock, mToken);

        Mockito.verify(merchantRestAPIMock).getCard(xauthCaptor.capture(),  responseCallbackCaptor.capture());

        responseCallbackCaptor.getValue().success(cardResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }


    @Test
    public void testPaymentGetCardsCardError() throws Exception {

        CardResponse cardResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getCards(merchantRestAPIMock, mToken);

        Mockito.verify(merchantRestAPIMock).getCard(xauthCaptor.capture(),  responseCallbackCaptor.capture());
        //when valid certification
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

        // when invalid certification
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
        Assert.assertNotNull(mSslHandshakeException);
    }


}
