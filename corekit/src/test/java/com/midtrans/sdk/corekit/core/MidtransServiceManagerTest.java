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
public class MidtransServiceManagerTest {

    private static final int ERR_TYPE_NPE = 1;
    @InjectMocks
    protected MidtransServiceCallbackImplement callbackImplement;
    @Mock
    private Context contextMock;
    @Mock
    private Resources resourcesMock;
    @Mock
    private MixpanelAnalyticsManager mixpanelAnalyticsManagerMock;
    @Mock
    private Token snapTokenMock;

    private String snapToken = "aa3afad7-a346-4db6-9cb3-737f24e4fc56";
    @Mock
    private CertPathValidatorException errorInvalidCertPatMock;
    @Mock
    private SSLHandshakeException errorInvalidSSLException;

    @Mock
    private Transaction transactionMock;
    @Mock
    private Throwable errorGeneralMock;
    @Captor
    private ArgumentCaptor<String> tokenIdCaptor;
    @Captor
    private ArgumentCaptor<TokenRequestModel> snapTokenRequestModelCaptor;
    @Mock
    private TokenRequestModel snapTokenRequestModelMock;
    private String transactionId = "trans_id";
    @Mock
    private CreditCardPaymentRequest creditcardRequestMock;
    @Captor
    private ArgumentCaptor<CreditCardPaymentRequest> creditCardRequestCaptor;
    @Mock
    private CreditCardPaymentRequest transactionRequestMock;
    @Captor
    private ArgumentCaptor<Boolean> pointCaptor;
    @Mock
    private TransactionResponse transactionResponseMock;
    @Mock
    private BankTransferPaymentRequest bankTransferRequestMock;
    @Captor
    private ArgumentCaptor<BankTransferPaymentRequest> bankTransferRequestCaptor;
    @Mock
    private KlikBCAPaymentRequest klikBCARequestMock;
    @Mock
    private BasePaymentRequest basePaymentRequestMock;
    @Captor
    private ArgumentCaptor<KlikBCAPaymentRequest> klikBCARequestCaptor;
    @Captor
    private ArgumentCaptor<BasePaymentRequest> BCAKlikpayRequestCaptor;
    @Mock
    private MandiriClickPayPaymentRequest mandiriClickPayPaymentRequestMock;
    @Captor
    private ArgumentCaptor<MandiriClickPayPaymentRequest> mandirClickPayPaymentRequestCaptor;
    @Captor
    private ArgumentCaptor<BasePaymentRequest> basePaymentRequestCaptor;
    @Mock
    private TelkomselEcashPaymentRequest telkomselEcashPaymentRequestMock;
    @Captor
    private ArgumentCaptor<TelkomselEcashPaymentRequest> telkomselEcashPaymentRequestCaptor;
    @Mock
    private IndosatDompetkuPaymentRequest indosatDompetkuPaymentRequestMock;
    @Captor
    private ArgumentCaptor<IndosatDompetkuPaymentRequest> indosatDompetKuPaymentRequestCaptor;
    private String sampleUserId = "user_id214";
    @Mock
    private ArrayList<SaveCardRequest> cardRequestListMock;
    @Captor
    private ArgumentCaptor<ArrayList<SaveCardRequest>> cardRequestListCaptor;

    // get card token properties
    private String saveCardResponse = "Created";
    @Captor
    private ArgumentCaptor<String> sampleUserIdCaptor;
    @Mock
    private ArrayList<SaveCardRequest> getCardResponseMock;
    @Mock
    private ArrayList<BankBinsResponse> getBankBinsResponseMock;

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
    private ArgumentCaptor<String> channelCator;
    @Captor
    private ArgumentCaptor<String> channelCaptor;
    @Captor
    private ArgumentCaptor<Double> grossAmount;
    @Captor
    private ArgumentCaptor<String> typeCaptor;

    //card registration properties
    @Captor
    private ArgumentCaptor<String> instalmentTermCaptor;
    @Captor
    private ArgumentCaptor<Boolean> scureCaptor;
    @Captor
    private ArgumentCaptor<Boolean> twoClickCaptor;
    @Captor
    private ArgumentCaptor<Double> grossAmountCaptor;
    @Captor
    private ArgumentCaptor<String> callbackArgumentCaptorCardNumber;
    @Captor
    private ArgumentCaptor<String> callbackArgumentCaptorCardCVV;
    @Captor
    private ArgumentCaptor<String> callbackArgumentCaptorCardYear;
    @Captor
    private ArgumentCaptor<String> callbackArgumentCaptorCardMonth;
    @Captor
    private ArgumentCaptor<String> callbackArgumentCaptorCar;
    @Captor
    private ArgumentCaptor<String> calbackArgumentCatorClientKey;

    @Captor
    private ArgumentCaptor<String> cardTokenCaptor;
    @Mock
    private BanksPointResponse BankPointsResponseMock;
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

    private Response<TokenDetailsResponse> createCardTokenResponse(Integer statusCode) {
        Request okReq = new Request.Builder().url(SDKConfigTest.PAPI_URL).build();
        okhttp3.Response okResponse = new okhttp3.Response.Builder().code((statusCode == null || statusCode > 300) ? 299 : statusCode).request(okReq).message("success").protocol(Protocol.HTTP_2).build();

        TokenDetailsResponse response = new TokenDetailsResponse();
        response.setStatusCode(String.valueOf(statusCode));

        Response<TokenDetailsResponse> resTransactionResponse = Response.success(response, okResponse);
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
     *  get card token
     */

    private void initGetCardToken(CardTokenRequest request, Boolean point, Boolean twoClick, Boolean secure) {

        Mockito.when(midtransServiceMock.getToken(
                SDKConfigTest.CARD_NUMBER,
                SDKConfigTest.CARD_CVV,
                SDKConfigTest.CARD_EXPIRY_MONTH,
                SDKConfigTest.CARD_EXPIRY_YEAR,
                SDKConfigTest.CLIENT_KEY,
                SDKConfigTest.CHANNEL,
                SDKConfigTest.TYPE,
                point
        )).thenReturn(callGetCardTokenMock);

        //with gross amount
        Mockito.when(midtransServiceMock.getToken(
                SDKConfigTest.CARD_NUMBER,
                SDKConfigTest.CARD_CVV,
                SDKConfigTest.CARD_EXPIRY_MONTH,
                SDKConfigTest.CARD_EXPIRY_YEAR,
                SDKConfigTest.CLIENT_KEY,
                SDKConfigTest.GROSS_AMOUNT,
                SDKConfigTest.CHANNEL,
                SDKConfigTest.TYPE,
                point
        )).thenReturn(callGetCardTokenMock);


        Mockito.when(midtransServiceMock.get3DSToken(
                SDKConfigTest.CARD_NUMBER,
                SDKConfigTest.CARD_CVV,
                SDKConfigTest.CARD_EXPIRY_MONTH,
                SDKConfigTest.CARD_EXPIRY_YEAR,
                SDKConfigTest.CLIENT_KEY,
                SDKConfigTest.BANK,
                secure,
                twoClick,
                SDKConfigTest.GROSS_AMOUNT,
                SDKConfigTest.CHANNEL,
                SDKConfigTest.TYPE,
                point
        )).thenReturn(callGetCardTokenMock);


        Mockito.when(midtransServiceMock.getTokenTwoClick(
                SDKConfigTest.CARD_CVV,
                SDKConfigTest.SAVED_TOKEN_ID,
                twoClick,
                secure,
                SDKConfigTest.GROSS_AMOUNT,
                SDKConfigTest.BANK,
                SDKConfigTest.CLIENT_KEY,
                SDKConfigTest.CHANNEL,
                SDKConfigTest.TYPE,
                point
        )).thenReturn(callGetCardTokenMock);

        Mockito.when(midtransServiceMock.getTokenInstalmentOfferTwoClick(
                SDKConfigTest.CARD_CVV,
                SDKConfigTest.SAVED_TOKEN_ID,
                twoClick,
                secure,
                SDKConfigTest.GROSS_AMOUNT,
                SDKConfigTest.BANK,
                SDKConfigTest.CLIENT_KEY,
                true,
                SDKConfigTest.INSTALLMENT_TERM,
                SDKConfigTest.CHANNEL,
                SDKConfigTest.TYPE,
                point
        )).thenReturn(callGetCardTokenMock);

        Mockito.when(midtransServiceMock.get3DSTokenInstalmentOffers(
                SDKConfigTest.CARD_NUMBER,
                SDKConfigTest.CARD_CVV,
                SDKConfigTest.CARD_EXPIRY_MONTH,
                SDKConfigTest.CARD_EXPIRY_YEAR,
                SDKConfigTest.CLIENT_KEY,
                SDKConfigTest.BANK,
                secure,
                twoClick,
                SDKConfigTest.GROSS_AMOUNT,
                true,
                SDKConfigTest.CHANNEL,
                SDKConfigTest.INSTALLMENT_TERM,
                SDKConfigTest.TYPE,
                point
        )).thenReturn(callGetCardTokenMock);

        callbackImplement.getCardToken(request);
        Mockito.verify(callGetCardTokenMock).enqueue(getCardTokenCaptor.capture());
    }
}
