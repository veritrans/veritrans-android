package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.content.res.Resources;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.midtrans.sdk.analytics.MixpanelAnalyticsManager;
import com.midtrans.sdk.corekit.SDKConfigTest;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TokenRequestModel;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.BanksPointResponse;
import com.midtrans.sdk.corekit.models.snap.Token;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.payment.BankTransferPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.BasePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.CreditCardPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.KlikBCAPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.MandiriClickPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.TelkomselEcashPaymentRequest;
import com.midtrans.sdk.corekit.utilities.CallbackCollaborator;
import com.midtrans.sdk.corekit.utilities.MerchantServiceCallbackImplement;
import com.securepreferences.SecurePreferences;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.cert.CertPathValidatorException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

import okhttp3.Protocol;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ziahaqi on 4/2/18.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, TextUtils.class, Logger.class, Looper.class, Base64.class,
        MixpanelAnalyticsManager.class, SdkUtil.class, MidtransRestAdapter.class})
@PowerMockIgnore("javax.net.ssl.*")
public class MerchantServiceManagerTest {

    private static final int ERR_TYPE_NPE = 1;
    @InjectMocks
    protected MerchantServiceCallbackImplement callbackImplement;
    @Mock
    private Context contextMock;
    @Mock
    private Resources resourcesMock;
    @Mock
    private MixpanelAnalyticsManager mixpanelAnalyticsManagerMock;
    @Mock
    private Token snapTokenMock;
    @Mock
    private CertPathValidatorException errorInvalidCertPatMock;
    @Mock
    private SSLHandshakeException errorInvalidSSLException;

    @Mock
    private Transaction transactionMock;
    @Mock
    private Throwable errorGeneralMock;
    @Mock
    private TokenRequestModel snapTokenRequestModelMock;

    @Mock
    private SecurePreferences preferencesMock;

    private MerchantServiceManager manager;

    @Mock
    MerchantApiService merchantApiServiceMock;

    private int timout = 1000;
    @Mock
    private CallbackCollaborator callbackCollaboratorMock;
    @Mock
    private Call<TokenDetailsResponse> callGetCardTokenMock;
    @Mock
    private Call<Token> callCheckoutMock;
    @Mock
    private TokenRequestModel checkoutRequestMock;
    @Mock
    private Call<String> callSaveCardMock;
    @Mock
    private Call<List<SaveCardRequest>> callGetCardsMock;


    @Captor
    private ArgumentCaptor<Callback<Token>> checkoutCaptor;
    @Captor
    private ArgumentCaptor<Callback<String>> saveCardCaptor;
    @Captor
    private ArgumentCaptor<Callback<List<SaveCardRequest>>> getCardsCaptor;


    @Test
    public void test() {
        Assert.assertEquals(1, 1);
    }

    @Before
    public void setup() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Looper.class);
        PowerMockito.mockStatic(Base64.class);
        PowerMockito.mockStatic(Logger.class);
        PowerMockito.mockStatic(MixpanelAnalyticsManager.class);
        PowerMockito.mockStatic(SdkUtil.class);
        PowerMockito.mockStatic(MidtransRestAdapter.class);

        Mockito.when(TextUtils.isEmpty(Matchers.anyString())).thenReturn(false);
        Mockito.when(TextUtils.isEmpty(null)).thenReturn(true);
        Mockito.when(TextUtils.isEmpty("")).thenReturn(true);

        Mockito.when(contextMock.getResources()).thenReturn(resourcesMock);
        Mockito.when(contextMock.getApplicationContext()).thenReturn(contextMock);
        Mockito.when(MidtransRestAdapter.newMerchantApiService(SDKConfigTest.MERCHANT_BASE_URL, timout)).thenReturn(merchantApiServiceMock);

        Mockito.when(SdkUtil.newPreferences(contextMock, "local.data")).thenReturn(preferencesMock);
        Mockito.when(SdkUtil.newMerchantServiceManager(SDKConfigTest.MERCHANT_BASE_URL, timout)).thenReturn(manager);

        manager = new MerchantServiceManager(MidtransRestAdapter.newMerchantApiService(SDKConfigTest.MERCHANT_BASE_URL, timout));
        callbackImplement = new MerchantServiceCallbackImplement(manager, callbackCollaboratorMock);

        SdkCoreFlowBuilder
                .init(contextMock, SDKConfigTest.CLIENT_KEY, SDKConfigTest.MERCHANT_BASE_URL)
                .buildSDK();


    }

    private Throwable createThrowableOfTransactionResponseFailure(int errorMessage) {
        switch (errorMessage) {
            case ERR_TYPE_NPE:
                return new Throwable(new NullPointerException());
        }

        return new Throwable();
    }

    private Response<Token> createCheckoutResponse(Integer statusCode, String snapToken, boolean emptyBody) {
        Request okReq = new Request.Builder().url(SDKConfigTest.PAPI_URL).build();
        okhttp3.Response okResponse = new okhttp3.Response.Builder().code((statusCode == null || statusCode > 300) ? 299 : statusCode).request(okReq).message("success").protocol(Protocol.HTTP_2).build();
        Token response = new Token();
        response.setTokenId(snapToken);

        Response<Token> resTransactionResponse = Response.success(emptyBody ? null : response, okResponse);
        return resTransactionResponse;
    }

    private Response<String> createSaveCardResponse(Integer statusCode, boolean emptyBody) {
        Request okReq = new Request.Builder().url(SDKConfigTest.PAPI_URL).build();
        okhttp3.Response okResponse = new okhttp3.Response.Builder().code((statusCode == null || statusCode > 300) ? 299 : statusCode).request(okReq).message("success").protocol(Protocol.HTTP_2).build();

        String response = "res";

        Response<String> resTransactionResponse = Response.success(emptyBody ? null : response, okResponse);
        return resTransactionResponse;
    }

    private Response<List<SaveCardRequest>> createGetCardsResponse(Integer statusCode, boolean emptyBody) {
        Request okReq = new Request.Builder().url(SDKConfigTest.PAPI_URL).build();
        okhttp3.Response okResponse = new okhttp3.Response.Builder().code((statusCode == null || statusCode > 300) ? 299 : statusCode).request(okReq).message("success").protocol(Protocol.HTTP_2).build();

        List<SaveCardRequest> response = createSampleCards();

        Response<List<SaveCardRequest>> resTransactionResponse = Response.success(emptyBody ? null : response, okResponse);
        return resTransactionResponse;
    }


    /**
     * checkout
     */

    private void initCheckout(TokenRequestModel model) {
        Mockito.when(merchantApiServiceMock.checkout(model)).thenReturn(callCheckoutMock);
        callbackImplement.checkout(model);
        Mockito.verify(callCheckoutMock).enqueue(checkoutCaptor.capture());
    }

    @Test
    public void checkoutSuccess() {
        initCheckout(checkoutRequestMock);
        Response<Token> response = createCheckoutResponse(200, SDKConfigTest.SNAP_TOKEN, false);
        checkoutCaptor.getValue().onResponse(callCheckoutMock, response);
        Mockito.verify(callbackCollaboratorMock).onCheckoutSuccess();
    }

    @Test
    public void checkoutFailure_whenEmptyStatusCode() {
        initCheckout(checkoutRequestMock);
        Response<Token> response = createCheckoutResponse(null, SDKConfigTest.SNAP_TOKEN, false);
        checkoutCaptor.getValue().onResponse(callCheckoutMock, response);
        Mockito.verify(callbackCollaboratorMock).onCheckoutSuccess();
    }

    @Test
    public void checkoutFailure_whenEmptySnapTokenNullOrEmpty() {
        initCheckout(checkoutRequestMock);
        Response<Token> response = createCheckoutResponse(240, null, false);
        checkoutCaptor.getValue().onResponse(callCheckoutMock, response);
        Mockito.verify(callbackCollaboratorMock).onCheckoutFailure();

        response = createCheckoutResponse(240, "", false);
        checkoutCaptor.getValue().onResponse(callCheckoutMock, response);
        Mockito.verify(callbackCollaboratorMock, Mockito.times(2)).onCheckoutFailure();
    }

    @Test
    public void checkoutFailure_whenStatusCodeNot200Or201() {
        initCheckout(checkoutRequestMock);
        Response<Token> response = createCheckoutResponse(204, SDKConfigTest.SNAP_TOKEN, false);
        checkoutCaptor.getValue().onResponse(callCheckoutMock, response);
        Mockito.verify(callbackCollaboratorMock).onCheckoutSuccess();
    }


    @Test
    public void checkoutFailure_whenServiceNull() {
        manager.setService(null);
        callbackImplement.checkout(checkoutRequestMock);
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingCreditCardError_whenEmptyResponseBody() {
        initCheckout(snapTokenRequestModelMock);
        checkoutCaptor.getValue().onResponse(callCheckoutMock, createCheckoutResponse(200, SDKConfigTest.SNAP_TOKEN, true));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void checkoutError() {
        initCheckout(snapTokenRequestModelMock);
        checkoutCaptor.getValue().onFailure(callCheckoutMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }


    /**
     * save cards
     */

    private void initSaveCard() {
        List<SaveCardRequest> cards = createSampleCards();

        Mockito.when(merchantApiServiceMock.saveCards(SDKConfigTest.USER_ID, cards)).thenReturn(callSaveCardMock);
        callbackImplement.saveCards(SDKConfigTest.USER_ID, cards);
        Mockito.verify(callSaveCardMock).enqueue(saveCardCaptor.capture());
    }

    private List<SaveCardRequest> createSampleCards() {
        List<SaveCardRequest> cards = new ArrayList<>();

        SaveCardRequest card = new SaveCardRequest(SDKConfigTest.SAVED_TOKEN_ID,
                SDKConfigTest.MASKED_CARD_NUMBER, SDKConfigTest.TWO_CLICK);
        cards.add(card);
        return cards;
    }

    @Test
    public void saveCardsSuccess() {
        initSaveCard();
        Response<String> response = createSaveCardResponse(200, false);
        saveCardCaptor.getValue().onResponse(callSaveCardMock, response);
        Mockito.verify(callbackCollaboratorMock).onSaveAndGetCardsSuccess();
    }

    @Test
    public void saveCardsFailure_whenEmptyStatusCode() {
        initSaveCard();
        Response<String> response = createSaveCardResponse(null, false);
        saveCardCaptor.getValue().onResponse(callSaveCardMock, response);
        Mockito.verify(callbackCollaboratorMock).onSaveAndGetCardsFailure();
    }


    @Test
    public void saveCardsFailure_whenStatusCodeNot200Or201() {
        initSaveCard();
        Response<String> response = createSaveCardResponse(204, false);
        saveCardCaptor.getValue().onResponse(callSaveCardMock, response);
        Mockito.verify(callbackCollaboratorMock).onSaveAndGetCardsFailure();
    }


    @Test
    public void saveCardsFailure_whenServiceNull() {
        manager.setService(null);
        callbackImplement.saveCards(SDKConfigTest.USER_ID, createSampleCards());
        Mockito.verify(callbackCollaboratorMock).onError();
    }


    @Test
    public void saveCardError() {
        initSaveCard();
        saveCardCaptor.getValue().onFailure(callSaveCardMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    /**
     * get Saved Cards
     */

    private void initGetCards() {
        Mockito.when(merchantApiServiceMock.getCards(SDKConfigTest.USER_ID)).thenReturn(callGetCardsMock);
        callbackImplement.getCards(SDKConfigTest.USER_ID);
        Mockito.verify(callGetCardsMock).enqueue(getCardsCaptor.capture());
    }

    @Test
    public void getCardsSuccess() {
        initGetCards();
        Response<List<SaveCardRequest>> response = createGetCardsResponse(200, false);
        getCardsCaptor.getValue().onResponse(callGetCardsMock, response);
        Mockito.verify(callbackCollaboratorMock).onSaveAndGetCardsSuccess();
    }

    @Test
    public void getCardsFailure_whenEmptyStatusCode() {
        initSaveCard();
        Response<String> response = createSaveCardResponse(null, false);
        saveCardCaptor.getValue().onResponse(callSaveCardMock, response);
        Mockito.verify(callbackCollaboratorMock).onSaveAndGetCardsFailure();
    }


    @Test
    public void getCardsFailure_whenStatusCodeNot200Or201() {
        initSaveCard();
        Response<String> response = createSaveCardResponse(204, false);
        saveCardCaptor.getValue().onResponse(callSaveCardMock, response);
        Mockito.verify(callbackCollaboratorMock).onSaveAndGetCardsFailure();
    }


    @Test
    public void getCardsFailure_whenServiceNull() {
        manager.setService(null);
        callbackImplement.getCards(SDKConfigTest.USER_ID);
        Mockito.verify(callbackCollaboratorMock).onError();
    }


    @Test
    public void getCardsError() {
        initGetCards();
        getCardsCaptor.getValue().onFailure(callGetCardsMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

}
