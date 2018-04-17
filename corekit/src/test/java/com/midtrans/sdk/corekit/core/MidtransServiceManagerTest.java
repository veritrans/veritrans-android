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
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.utilities.CallbackCollaborator;
import com.midtrans.sdk.corekit.utilities.MidtransServiceCallbackImplement;
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
public class MidtransServiceManagerTest {

    private static final int ERR_TYPE_NPE = 1;
    @InjectMocks
    protected MidtransServiceCallbackImplement callbackImplement;
    @Mock
    private Context contextMock;
    @Mock
    private Resources resourcesMock;


    @Mock
    private SecurePreferences preferencesMock;

    private MidtransServiceManager midtransServiceManager;

    @Mock
    MidtransApiService midtransServiceMock;

    private int timout = 1000;

    @Mock
    private Call<CardRegistrationResponse> callRegisterCardMock;
    @Mock
    private CallbackCollaborator callbackCollaboratorMock;
    @Mock
    private Call<TokenDetailsResponse> callGetCardTokenMock;

    @Captor
    private ArgumentCaptor<Callback<CardRegistrationResponse>> cardRegistrationCaptor;
    @Captor
    private ArgumentCaptor<Callback<TokenDetailsResponse>> getCardTokenCaptor;


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
        Mockito.when(MidtransRestAdapter.newMidtransApiService(timout)).thenReturn(midtransServiceMock);

        Mockito.when(SdkUtil.newPreferences(contextMock, "local.data")).thenReturn(preferencesMock);
        Mockito.when(SdkUtil.newMidtransServiceManager(timout)).thenReturn(midtransServiceManager);

        midtransServiceManager = new MidtransServiceManager(MidtransRestAdapter.newMidtransApiService(timout));
        callbackImplement = new MidtransServiceCallbackImplement(midtransServiceManager, callbackCollaboratorMock);

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

    private Response<CardRegistrationResponse> createCarRegistrationResponse(Integer statusCode) {
        Request okReq = new Request.Builder().url(SDKConfigTest.PAPI_URL).build();
        okhttp3.Response okResponse = new okhttp3.Response.Builder().code((statusCode == null || statusCode > 300) ? 299 : statusCode).request(okReq).message("success").protocol(Protocol.HTTP_2).build();

        CardRegistrationResponse response = new CardRegistrationResponse();
        response.setStatusCode(String.valueOf(statusCode));

        Response<CardRegistrationResponse> resTransactionResponse = Response.success(response, okResponse);
        return resTransactionResponse;

    }

    private Response<TokenDetailsResponse> createCardTokenResponse(Integer statusCode, boolean emptyBody) {
        Request okReq = new Request.Builder().url(SDKConfigTest.PAPI_URL).build();
        okhttp3.Response okResponse = new okhttp3.Response.Builder().code((statusCode == null || statusCode > 300) ? 299 : statusCode).request(okReq).message("success").protocol(Protocol.HTTP_2).build();

        TokenDetailsResponse response = new TokenDetailsResponse();
        response.setStatusCode(String.valueOf(statusCode));

        Response<TokenDetailsResponse> resTransactionResponse = Response.success(emptyBody ? null : response, okResponse);
        return resTransactionResponse;

    }


    /**
     * Register Card
     */

    private void initCardRegistration() {

        Mockito.when(midtransServiceMock.registerCard(SDKConfigTest.CARD_NUMBER,
                SDKConfigTest.CARD_CVV,
                SDKConfigTest.CARD_EXPIRY_MONTH,
                SDKConfigTest.CARD_EXPIRY_YEAR,
                SDKConfigTest.CLIENT_KEY)).thenReturn(callRegisterCardMock);

        callbackImplement.registerCard(SDKConfigTest.CARD_NUMBER,
                SDKConfigTest.CARD_CVV,
                SDKConfigTest.CARD_EXPIRY_MONTH,
                SDKConfigTest.CARD_EXPIRY_YEAR,
                SDKConfigTest.CLIENT_KEY);

        Mockito.verify(callRegisterCardMock).enqueue(cardRegistrationCaptor.capture());
    }

    @Test
    public void registerCardSuccess() {
        initCardRegistration();
        Response<CardRegistrationResponse> response = createCarRegistrationResponse(200);
        cardRegistrationCaptor.getValue().onResponse(callRegisterCardMock, response);
        Mockito.verify(callbackCollaboratorMock).onCardRegistrationSuccess();
    }


    @Test
    public void getBankPointsFailure_whenEmptyStatusCode() {
        initCardRegistration();
        Response<CardRegistrationResponse> response = createCarRegistrationResponse(null);
        cardRegistrationCaptor.getValue().onResponse(callRegisterCardMock, response);
        Mockito.verify(callbackCollaboratorMock).onCardRegistrationFailed();
    }

    @Test
    public void getBankPointsFailure_whenStatusCodeNot200Or201() {
        initCardRegistration();
        Response<CardRegistrationResponse> response = createCarRegistrationResponse(214);
        cardRegistrationCaptor.getValue().onResponse(callRegisterCardMock, response);
        Mockito.verify(callbackCollaboratorMock).onCardRegistrationFailed();
    }


    @Test
    public void getBankPointsFailure_whenServiceNull() {
        midtransServiceManager.setService(null);

        callbackImplement.registerCard(SDKConfigTest.CARD_NUMBER,
                SDKConfigTest.CARD_CVV,
                SDKConfigTest.CARD_EXPIRY_MONTH,
                SDKConfigTest.CARD_EXPIRY_YEAR,
                SDKConfigTest.CLIENT_KEY);

        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void getBankPointsError() {
        initCardRegistration();
        cardRegistrationCaptor.getValue().onFailure(callRegisterCardMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    /**
     * get card token normal
     */
    private void initGetCardTokenNormal() {

        Mockito.when(midtransServiceMock.getToken(
                SDKConfigTest.CARD_NUMBER,
                SDKConfigTest.CARD_CVV,
                SDKConfigTest.CARD_EXPIRY_MONTH,
                SDKConfigTest.CARD_EXPIRY_YEAR,
                SDKConfigTest.CLIENT_KEY,
                SDKConfigTest.GROSS_AMOUNT,
                SDKConfigTest.CHANNEL,
                SDKConfigTest.TYPE,
                false
        )).thenReturn(callGetCardTokenMock);

        CardTokenRequest req = new CardTokenRequest(
                SDKConfigTest.CARD_NUMBER,
                SDKConfigTest.CARD_CVV,
                SDKConfigTest.CARD_EXPIRY_MONTH,
                SDKConfigTest.CARD_EXPIRY_YEAR,
                SDKConfigTest.CLIENT_KEY);

        req.setChannel(SDKConfigTest.CHANNEL);
        req.setGrossAmount(SDKConfigTest.GROSS_AMOUNT);
        req.setType(SDKConfigTest.TYPE);

        req.setPoint(false);
        req.setTwoClick(false);
        req.setInstallment(false);
        req.setSecure(false);
        callbackImplement.getCardToken(req);

        Mockito.verify(callGetCardTokenMock).enqueue(getCardTokenCaptor.capture());
    }

    /**
     * get card token normal
     */
    private void initGetCardTokenNormal3ds() {

        Mockito.when(midtransServiceMock.get3DSToken(
                SDKConfigTest.CARD_NUMBER,
                SDKConfigTest.CARD_CVV,
                SDKConfigTest.CARD_EXPIRY_MONTH,
                SDKConfigTest.CARD_EXPIRY_YEAR,
                SDKConfigTest.CLIENT_KEY,
                SDKConfigTest.BANK,
                true,
                false,
                SDKConfigTest.GROSS_AMOUNT,
                SDKConfigTest.CHANNEL,
                SDKConfigTest.TYPE,
                false
        )).thenReturn(callGetCardTokenMock);

        CardTokenRequest req = new CardTokenRequest(
                SDKConfigTest.CARD_NUMBER,
                SDKConfigTest.CARD_CVV,
                SDKConfigTest.CARD_EXPIRY_MONTH,
                SDKConfigTest.CARD_EXPIRY_YEAR,
                SDKConfigTest.CLIENT_KEY);

        req.setChannel(SDKConfigTest.CHANNEL);
        req.setGrossAmount(SDKConfigTest.GROSS_AMOUNT);
        req.setType(SDKConfigTest.TYPE);
        req.setBank(SDKConfigTest.BANK);
        req.setPoint(false);
        req.setTwoClick(false);
        req.setInstallment(false);
        req.setSecure(true);
        callbackImplement.getCardToken(req);
    }

    @Test
    public void getCardTokenNormal() {
        initGetCardTokenNormal();
        Mockito.verify(callGetCardTokenMock).enqueue(getCardTokenCaptor.capture());

    }

    @Test
    public void getCardTokenNormal_With3Ds() {
        initGetCardTokenNormal3ds();
        Mockito.verify(callGetCardTokenMock).enqueue(getCardTokenCaptor.capture());
    }

    @Test
    public void getCardTokenNormal_With3DsAndInstallment() {

        Mockito.when(midtransServiceMock.get3DSTokenInstalmentOffers(
                SDKConfigTest.CARD_NUMBER,
                SDKConfigTest.CARD_CVV,
                SDKConfigTest.CARD_EXPIRY_MONTH,
                SDKConfigTest.CARD_EXPIRY_YEAR,
                SDKConfigTest.CLIENT_KEY,
                SDKConfigTest.BANK,
                true,
                false,
                SDKConfigTest.GROSS_AMOUNT,
                true,
                SDKConfigTest.CHANNEL,
                SDKConfigTest.INSTALLMENT_TERM,
                SDKConfigTest.TYPE,
                false
        )).thenReturn(callGetCardTokenMock);

        CardTokenRequest req = new CardTokenRequest(
                SDKConfigTest.CARD_NUMBER,
                SDKConfigTest.CARD_CVV,
                SDKConfigTest.CARD_EXPIRY_MONTH,
                SDKConfigTest.CARD_EXPIRY_YEAR,
                SDKConfigTest.CLIENT_KEY);

        req.setChannel(SDKConfigTest.CHANNEL);
        req.setGrossAmount(SDKConfigTest.GROSS_AMOUNT);
        req.setType(SDKConfigTest.TYPE);
        req.setBank(SDKConfigTest.BANK);
        req.setPoint(false);
        req.setTwoClick(false);
        req.setInstallment(false);
        req.setSecure(true);
        req.setInstalmentTerm(Integer.parseInt(SDKConfigTest.INSTALLMENT_TERM));
        req.setInstallment(true);
        callbackImplement.getCardToken(req);

        Mockito.verify(callGetCardTokenMock).enqueue(getCardTokenCaptor.capture());
    }

    @Test
    public void getCardTokenTwoClick() {
        Mockito.when(midtransServiceMock.getTokenTwoClick(
                SDKConfigTest.CARD_CVV,
                SDKConfigTest.SAVED_TOKEN_ID,
                true,
                true,
                SDKConfigTest.GROSS_AMOUNT,
                SDKConfigTest.BANK,
                SDKConfigTest.CLIENT_KEY,
                SDKConfigTest.CHANNEL,
                SDKConfigTest.TYPE,
                false
        )).thenReturn(callGetCardTokenMock);

        CardTokenRequest req = new CardTokenRequest();

        req.setCardCVV(SDKConfigTest.CARD_CVV);
        req.setSavedTokenId(SDKConfigTest.SAVED_TOKEN_ID);
        req.setTwoClick(true);
        req.setSecure(true);
        req.setGrossAmount(SDKConfigTest.GROSS_AMOUNT);
        req.setBank(SDKConfigTest.BANK);
        req.setClientKey(SDKConfigTest.CLIENT_KEY);
        req.setChannel(SDKConfigTest.CHANNEL);
        req.setType(SDKConfigTest.TYPE);
        req.setPoint(false);

        callbackImplement.getCardToken(req);
        Mockito.verify(callGetCardTokenMock).enqueue(getCardTokenCaptor.capture());
    }

    @Test
    public void getCardTokenTwoClick_withInstallment() {

        Mockito.when(midtransServiceMock.getTokenInstalmentOfferTwoClick(
                SDKConfigTest.CARD_CVV,
                SDKConfigTest.SAVED_TOKEN_ID,
                true,
                true,
                SDKConfigTest.GROSS_AMOUNT,
                SDKConfigTest.BANK,
                SDKConfigTest.CLIENT_KEY,
                true,
                SDKConfigTest.INSTALLMENT_TERM,
                SDKConfigTest.CHANNEL,
                SDKConfigTest.TYPE,
                false
        )).thenReturn(callGetCardTokenMock);

        CardTokenRequest req = new CardTokenRequest();

        req.setCardCVV(SDKConfigTest.CARD_CVV);
        req.setSavedTokenId(SDKConfigTest.SAVED_TOKEN_ID);
        req.setTwoClick(true);
        req.setSecure(true);
        req.setGrossAmount(SDKConfigTest.GROSS_AMOUNT);
        req.setBank(SDKConfigTest.BANK);
        req.setClientKey(SDKConfigTest.CLIENT_KEY);
        req.setChannel(SDKConfigTest.CHANNEL);
        req.setType(SDKConfigTest.TYPE);
        req.setPoint(false);
        req.setInstallment(true);
        req.setInstalmentTerm(Integer.valueOf(SDKConfigTest.INSTALLMENT_TERM));

        callbackImplement.getCardToken(req);
        Mockito.verify(callGetCardTokenMock).enqueue(getCardTokenCaptor.capture());
    }

    @Test
    public void getCardToken_whenServiceNull() {
        CardTokenRequest req = new CardTokenRequest();
        midtransServiceManager.setService(null);
        callbackImplement.getCardToken(req);
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void getCardTokenSuccess() {
        initGetCardTokenNormal();
        Response<TokenDetailsResponse> response = createCardTokenResponse(200, false);
        getCardTokenCaptor.getValue().onResponse(callGetCardTokenMock, response);
        Mockito.verify(callbackCollaboratorMock).onGetCardTokenSuccess();
    }

    @Test
    public void getCardTokenFailure_whenStatusMessageEmpty() {
        initGetCardTokenNormal();
        Response<TokenDetailsResponse> response = createCardTokenResponse(201, false);
        getCardTokenCaptor.getValue().onResponse(callGetCardTokenMock, response);
        Mockito.verify(callbackCollaboratorMock).onGetCardTokenFailed();
    }

    @Test
    public void getCardTokenFailure_whenStatusNotEmpty() {
        initGetCardTokenNormal();
        Response<TokenDetailsResponse> response = createCardTokenResponse(201, false);
        response.body().setStatusMessage("message not empty");
        getCardTokenCaptor.getValue().onResponse(callGetCardTokenMock, response);
        Mockito.verify(callbackCollaboratorMock).onGetCardTokenFailed();
    }

    @Test
    public void getCardTokenError_whenResponBodyEmpty() {
        initGetCardTokenNormal();
        Response<TokenDetailsResponse> response = createCardTokenResponse(234, true);
        getCardTokenCaptor.getValue().onResponse(callGetCardTokenMock, response);
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void getCardTokenError() {
        initGetCardTokenNormal();
        getCardTokenCaptor.getValue().onFailure(callGetCardTokenMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }
}
