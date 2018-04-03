package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.content.res.Resources;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.midtrans.sdk.analytics.MixpanelAnalyticsManager;
import com.midtrans.sdk.corekit.SDKConfigTest;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.TokenRequestModel;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.BanksPointResponse;
import com.midtrans.sdk.corekit.models.snap.Token;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.TransactionDetails;
import com.midtrans.sdk.corekit.models.snap.payment.BankTransferPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.BasePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.CreditCardPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GCIPaymentRequest;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.cert.CertPathValidatorException;
import java.util.ArrayList;

import javax.net.ssl.SSLHandshakeException;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by ziahaqi on 4/2/18.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, TextUtils.class, Logger.class, Looper.class, Base64.class,
        MixpanelAnalyticsManager.class, SdkUtil.class, MidtransRestAdapter.class})
@PowerMockIgnore("javax.net.ssl.*")
public class SnapServiceManagerTest {

    public static final String CARD_NUMBER = "4811111111111114";
    public static final String CARD_CVV = "123";
    public static final String CARD_EXP_MONTH = "123";
    public static final String CARD_EXP_YEAR = "123";
    private static final String RESPONSE_CODE_200 = "200";
    private static final String RESPONSE_CODE_400 = "400";
    protected String sampleJsonResponse = "{\"response\":\"response\"}";
    // transaction properties
    @InjectMocks
    protected SnapServiceCallbackImplement callbackImplement;
    CardTokenRequest cardTokenRequest = new CardTokenRequest();
    @Mock
    private Context contextMock;
    @Mock
    private Resources resourcesMock;
    private MidtransSDK midtransSDK;
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
    private java.lang.Throwable errorGeneralMock;
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

    @Mock
    private GCIPaymentRequest gciPaymentRequestMock;
    @Captor
    private ArgumentCaptor<GCIPaymentRequest> gciCaptor;

    @Mock
    private TransactionDetails transactionDetailsMock;

    @Captor
    private ArgumentCaptor<String> cardTokenCaptor;
    @Mock
    private BanksPointResponse BankPointsResponseMock;
    @Mock
    private SecurePreferences preferencesMock;

    private SnapServiceManager snapTransactionManager;
    @Mock
    private MidtransServiceManager midtransServiceManagerMock;
    @Mock
    private MerchantServiceManager merchantServiceManagerMock;

    @Mock
    SnapApiService snapApiServiceMock;

    private int timout = 1000;

    @Mock
    private Call<Transaction> callMock;

    @Captor
    private ArgumentCaptor<retrofit2.Callback<Transaction>> transactionCaptor;
    @Mock
    private CallbackCollaborator callbackCollaboratorMock;

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

        Mockito.when(contextMock.getResources()).thenReturn(resourcesMock);
        Mockito.when(contextMock.getApplicationContext()).thenReturn(contextMock);
        Mockito.when(MidtransRestAdapter.newSnapApiService(timout)).thenReturn(snapApiServiceMock);

        Mockito.when(SdkUtil.newPreferences(contextMock, "local.data")).thenReturn(preferencesMock);
        Mockito.when(SdkUtil.newSnapServiceManager(timout)).thenReturn(snapTransactionManager);
        Mockito.when(SdkUtil.newMidtransServiceManager(timout)).thenReturn(midtransServiceManagerMock);
        Mockito.when(SdkUtil.newMerchantServiceManager(SDKConfigTest.MERCHANT_BASE_URL, timout)).thenReturn(merchantServiceManagerMock);

        snapTransactionManager = new SnapServiceManager(MidtransRestAdapter.newSnapApiService(timout));
        callbackImplement = new SnapServiceCallbackImplement(snapTransactionManager, callbackCollaboratorMock);

        midtransSDK = SdkCoreFlowBuilder
                .init(contextMock, SDKConfigTest.CLIENT_KEY, SDKConfigTest.MERCHANT_BASE_URL)
                .buildSDK();


    }

    private Response<Transaction> createResponseTransactionSuccess(int statusCode, boolean bodyEmpty) {
        Request okReq = new Request.Builder().url(SDKConfigTest.SNAP_URL).build();
        okhttp3.Response okResponse = new okhttp3.Response.Builder().code(statusCode).request(okReq).message("success").protocol(Protocol.HTTP_2).build();
        Transaction transaction = bodyEmpty ? null : createTransactionSuccess();
        Response<Transaction> response = Response.success(transaction, okResponse);
        return response;
    }

    private Response<Transaction> createResponseTransactionError(int statusCode) {
        ResponseBody responseBody = ResponseBody.create(MediaType.parse("Exeption"), "error");
        Response<Transaction> response = Response.error(statusCode, responseBody);
        return response;
    }

    private Transaction createTransactionSuccess() {
        Transaction transaction = new Transaction();
        transaction.setToken(snapToken);
        return transaction;
    }


    private void initGetTransactions() {
        Mockito.when(snapApiServiceMock.getPaymentOption(snapToken)).thenReturn(callMock);
        callbackImplement.getTransactionOptions(snapToken);
        Mockito.verify(callMock).enqueue(transactionCaptor.capture());
    }

    @Test
    public void getTransactionOptionSuccess() {
        initGetTransactions();
        transactionCaptor.getValue().onResponse(callMock, createResponseTransactionSuccess(200, false));
        Mockito.verify(callbackCollaboratorMock).onGetPaymentOptionSuccess();
    }

    @Test
    public void getTransactionOptionFailure_whenResponseCodeNot200() {
        initGetTransactions();
        transactionCaptor.getValue().onResponse(callMock, createResponseTransactionSuccess(234, false));
        Mockito.verify(callbackCollaboratorMock).onGetPaymentOptionFailure();
    }

    @Test
    public void getTransactionOptionFailure_whenResponsCodeNot200AndSnapTokenEmpty() {
        initGetTransactions();
        Response<Transaction> resTransaction = createResponseTransactionSuccess(234, false);
        Mockito.when(Mockito.spy(resTransaction.body()).getToken()).thenReturn(null);
        transactionCaptor.getValue().onResponse(callMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onGetPaymentOptionFailure();
    }

    @Test
    public void getTransactionOptionError_whenResponseCodeNotIn200To300() {
        initGetTransactions();
        transactionCaptor.getValue().onResponse(callMock, createResponseTransactionError(400));
        Mockito.verify(callbackCollaboratorMock).onError();
    }


}
