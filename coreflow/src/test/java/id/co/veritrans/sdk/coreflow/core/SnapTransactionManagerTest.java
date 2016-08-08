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
import id.co.veritrans.sdk.coreflow.models.SnapTokenRequestModel;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.models.snap.Token;
import id.co.veritrans.sdk.coreflow.models.snap.Transaction;
import id.co.veritrans.sdk.coreflow.models.snap.TransactionData;
import id.co.veritrans.sdk.coreflow.models.snap.payment.BankTransferPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.BasePaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.CreditCardPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.IndosatDompetkuPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.KlikBCAPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.MandiriClickPayPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.TelkomselEcashPaymentRequest;
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
    protected BusCollaborator busCollaborator;
    @InjectMocks
    protected EventBusImplementSample eventBusImplementSample;
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
    private Transaction transactionMock;
    @Mock
    private java.lang.Throwable errorGeneralMock;
    @Captor
    private ArgumentCaptor<String> tokenIdCaptor;
    @Captor
    private ArgumentCaptor<SnapTokenRequestModel> snapTokenRequestModelCaptor;
    @Mock
    private SnapTokenRequestModel snapTokenRequestModelMock;
    @Mock
    private TransactionData transactionDataMock;
    private String transactionId = "trans_id";

    @Mock
    private CreditCardPaymentRequest creditcardRequestMock;
    @Captor
    private ArgumentCaptor<CreditCardPaymentRequest> creditCardRequestCaptor;
    @Mock
    private CreditCardPaymentRequest transactionRequestMock;
    @Captor
    private ArgumentCaptor<Callback<TransactionResponse>> transactionResponseCallbackCaptor;
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
        eventBusImplementSample.setTransactionManager(transactionManager);

    }

    /**
     * Get Snap token test
     */
    @Test
    public void getTokenTestSuccess(){
        Mockito.when(snapTokenMock.getTokenId()).thenReturn(tokenId);
        eventBusImplementSample.getSnapToken(merchantApi, snapTokenRequestModelMock);

        Mockito.verify(merchantApi, Mockito.times(1)).getSnapToken(snapTokenRequestModelCaptor.capture(),
                callbackSnapTokenResponseCaptor.capture());

        callbackSnapTokenResponseCaptor.getValue().success(snapTokenMock, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetSnapTokenSuccess();
    }

    @Test
    public void getTokenTestSuccess_whenTokenNullOrEmpty(){
        Mockito.when(snapTokenMock.getTokenId()).thenReturn("");
        eventBusImplementSample.getSnapToken(merchantApi, snapTokenRequestModelMock);

        Mockito.verify(merchantApi, Mockito.times(1)).getSnapToken(snapTokenRequestModelCaptor.capture(),
                callbackSnapTokenResponseCaptor.capture());

        callbackSnapTokenResponseCaptor.getValue().success(snapTokenMock, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetSnapTokenFailed();
    }



    @Test
    public void getTokenTestSuccess_whenResponseNull(){
        Mockito.when(snapTokenMock.getTokenId()).thenReturn(tokenId);
        eventBusImplementSample.getSnapToken(merchantApi, snapTokenRequestModelMock);

        Mockito.verify(merchantApi, Mockito.times(1)).getSnapToken(snapTokenRequestModelCaptor.capture(),
                callbackSnapTokenResponseCaptor.capture());

        callbackSnapTokenResponseCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }

    @Test
    public void getTokenTestError_whenValidSSL(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.getSnapToken(merchantApi, snapTokenRequestModelMock);

        Mockito.verify(merchantApi, Mockito.times(1)).getSnapToken(snapTokenRequestModelCaptor.capture(),
                callbackSnapTokenResponseCaptor.capture());

        callbackSnapTokenResponseCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();
    }

    @Test
    public void getTokenTestError_whenInvalidSSL(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.getSnapToken(merchantApi, snapTokenRequestModelMock);

        Mockito.verify(merchantApi, Mockito.times(1)).getSnapToken(snapTokenRequestModelCaptor.capture(),
                callbackSnapTokenResponseCaptor.capture());

        callbackSnapTokenResponseCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();
    }

    @Test
    public void getTokenTestError(){
        eventBusImplementSample.getSnapToken(merchantApi, snapTokenRequestModelMock);

        Mockito.verify(merchantApi, Mockito.times(1)).getSnapToken(snapTokenRequestModelCaptor.capture(),
                callbackSnapTokenResponseCaptor.capture());
        callbackSnapTokenResponseCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }


    /**
     * Get Payment Type
     */

    @Test
    public void getSnapToken_whenTokenNull(){
        eventBusImplementSample.getPaymentType(snapAPI, null);

        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void getSnapTokenSuccess_whenResponseNull(){
        eventBusImplementSample.getPaymentType(snapAPI, tokenId);

        Mockito.verify(snapAPI).getSnapTransaction(tokenIdCaptor.capture(), transactionCaptorMock.capture());
        transactionCaptorMock.getValue().success(null, retrofitResponse);

        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void getSnapTransactionSuccess(){
        Mockito.when(transactionMock.getTransactionData()).thenReturn(transactionDataMock);
        Mockito.when(transactionDataMock.getTransactionId()).thenReturn(transactionId);

        eventBusImplementSample.getPaymentType(snapAPI, tokenId);

        Mockito.verify(snapAPI).getSnapTransaction(tokenIdCaptor.capture(), transactionCaptorMock.capture());
        transactionCaptorMock.getValue().success(transactionMock, retrofitResponse);

        Mockito.verify(busCollaborator).onGetSnapTransactionSuccess();
    }

    @Test
    public void getSnapTransactionSuccess_whenTransIdEmpty(){
        Mockito.when(transactionMock.getTransactionData()).thenReturn(transactionDataMock);
        Mockito.when(transactionDataMock.getTransactionId()).thenReturn("");

        eventBusImplementSample.getPaymentType(snapAPI, tokenId);

        Mockito.verify(snapAPI).getSnapTransaction(tokenIdCaptor.capture(), transactionCaptorMock.capture());
        transactionCaptorMock.getValue().success(transactionMock, retrofitResponse);

        Mockito.verify(busCollaborator).onGetSnapTransactionFailed();
    }

    @Test
    public void getSnapTransactionSuccess_whenCodeNot200(){

        retrofitResponse = new Response("URL", 300, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

        eventBusImplementSample.getPaymentType(snapAPI, tokenId);

        Mockito.verify(snapAPI).getSnapTransaction(tokenIdCaptor.capture(), transactionCaptorMock.capture());
        transactionCaptorMock.getValue().success(transactionMock, retrofitResponse);

        Mockito.verify(busCollaborator).onGetSnapTransactionFailed();
    }

    @Test
    public void getSnapTransactionSuccess_whenResponseNull(){

        retrofitResponse = new Response("URL", 300, "success", Collections.EMPTY_LIST,
                new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));

        eventBusImplementSample.getPaymentType(snapAPI, tokenId);

        Mockito.verify(snapAPI).getSnapTransaction(tokenIdCaptor.capture(), transactionCaptorMock.capture());
        transactionCaptorMock.getValue().success(null, retrofitResponse);

        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }



    @Test
    public void getSnapTokenError_whenCertPathInvalid(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.getPaymentType(snapAPI, tokenId);

        Mockito.verify(snapAPI).getSnapTransaction(tokenIdCaptor.capture(), transactionCaptorMock.capture());
        transactionCaptorMock.getValue().failure(retrofitError);

        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void getSnapTokenError_whenInvalidSSL(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.getPaymentType(snapAPI, tokenId);
        Mockito.verify(snapAPI).getSnapTransaction(tokenIdCaptor.capture(), transactionCaptorMock.capture());
        transactionCaptorMock.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void getSnapTokenError_whenGeneralError(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.getPaymentType(snapAPI, tokenId);
        Mockito.verify(snapAPI).getSnapTransaction(tokenIdCaptor.capture(), transactionCaptorMock.capture());
        transactionCaptorMock.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    /**
     * Payment using Credit Card.
     */

    @Test
    public void paymentUsingCreditCard_whenRequestNull(){
        eventBusImplementSample.paymentUsingSnapCreditCard(snapAPI, null);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingCreditCardSuccess_whenResponseNull(){
        eventBusImplementSample.paymentUsingSnapCreditCard(snapAPI, creditcardRequestMock);
        Mockito.verify(snapAPI).paymentUsingCreditCard(creditCardRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingCreditCardSuccess_whenCode200(){
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("200");
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.paymentUsingSnapCreditCard(snapAPI, transactionRequestMock);
        Mockito.verify(snapAPI).paymentUsingCreditCard(creditCardRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionSuccessEvent();
    }

    @Test
    public void paymentUsingCreditCardSuccess_whenCodeNot200(){
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("300");
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.paymentUsingSnapCreditCard(snapAPI, transactionRequestMock);
        Mockito.verify(snapAPI).paymentUsingCreditCard(creditCardRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionFailedEvent();
    }

    @Test
    public void paymentUsingCreditCardError_whenGeneralError(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.paymentUsingSnapCreditCard(snapAPI, transactionRequestMock);
        Mockito.verify(snapAPI).paymentUsingCreditCard(creditCardRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingCreditCardError_whenInvalidSSL(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.paymentUsingSnapCreditCard(snapAPI, transactionRequestMock);
        Mockito.verify(snapAPI).paymentUsingCreditCard(creditCardRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void paymentUsingCreditCardError_whenInvalidCertPath(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.paymentUsingSnapCreditCard(snapAPI, transactionRequestMock);
        Mockito.verify(snapAPI).paymentUsingCreditCard(creditCardRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    /**
     * Payment Using Bank Transfer BCA
     */
    @Test
    public void paymentUsingBankTransfer_whenRequestNull(){
        eventBusImplementSample.paymentUsingSnapBankTransferBCA(snapAPI, null);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }


    @Test
    public void paymentUsingBankTransferSuccess_whenResponseNull(){
        eventBusImplementSample.paymentUsingSnapBankTransferBCA(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferBCA(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingBankTransferSuccess_whenCode200(){
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("200");
        eventBusImplementSample.paymentUsingSnapBankTransferBCA(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferBCA(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionSuccessEvent();
    }

    @Test
    public void paymentUsingBankTransferSuccess_whenCodeNot200(){
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("300");
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.paymentUsingSnapBankTransferBCA(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferBCA(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionFailedEvent();
    }

    @Test
    public void paymentUsingBankTransferError_whenGeneralError(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.paymentUsingSnapBankTransferBCA(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferBCA(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingBankTransferError_whenInvalidSSL(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.paymentUsingSnapBankTransferBCA(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferBCA(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void paymentUsingBankTransferError_whenInvalidCertPath(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.paymentUsingSnapBankTransferBCA(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferBCA(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    /**
     * Payment Using Bank Transfer Permata.
     */
    @Test
    public void paymentUsingBankTransferPermata_whenRequestNull() {
        eventBusImplementSample.paymentUsingSnapBankTransferPermata(snapAPI, null);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }


    @Test
    public void paymentUsingBankTransferPermataSuccess_whenResponseNull() {
        eventBusImplementSample.paymentUsingSnapBankTransferPermata(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferPermata(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingBankTransferPermataSuccess_whenCode200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("200");
        eventBusImplementSample.paymentUsingSnapBankTransferPermata(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferPermata(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionSuccessEvent();
    }

    @Test
    public void paymentUsingBankTransferPermataSuccess_whenCodeNot200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("300");
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.paymentUsingSnapBankTransferPermata(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferPermata(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionFailedEvent();
    }

    @Test
    public void paymentUsingBankTransferPermataError_whenGeneralError() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.paymentUsingSnapBankTransferPermata(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferPermata(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingBankTransferPermataError_whenInvalidSSL() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.paymentUsingSnapBankTransferBCA(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferBCA(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void paymentUsingBankTransferPermataError_whenInvalidCertPath() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.paymentUsingSnapBankTransferPermata(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferPermata(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    /**
     * Payment using KlikBCA
     */
    @Test
    public void paymentUsingKlikBCA_whenRequestNull(){
        eventBusImplementSample.paymentUsingSnapKlikBCA(snapAPI, null);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }


    @Test
    public void paymentUsingKlikBCASuccess_whenResponseNull(){
        eventBusImplementSample.paymentUsingSnapKlikBCA(snapAPI, klikBCARequestMock);
        Mockito.verify(snapAPI).paymentUsingKlikBCA(klikBCARequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingKlikBCASuccess_whenCode200(){
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("200");
        eventBusImplementSample.paymentUsingSnapKlikBCA(snapAPI, klikBCARequestMock);
        Mockito.verify(snapAPI).paymentUsingKlikBCA(klikBCARequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionSuccessEvent();
    }

    @Test
    public void paymentUsingKlikBCASuccess_whenCodeNot200(){
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("300");
        eventBusImplementSample.paymentUsingSnapKlikBCA(snapAPI, klikBCARequestMock);
        Mockito.verify(snapAPI).paymentUsingKlikBCA(klikBCARequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionFailedEvent();
    }

    @Test
    public void paymentUsingKlikBCAError_whenGeneralError(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.paymentUsingSnapKlikBCA(snapAPI, klikBCARequestMock);
        Mockito.verify(snapAPI).paymentUsingKlikBCA(klikBCARequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingKlikBCAError_whenInvalidSSL(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.paymentUsingSnapKlikBCA(snapAPI, klikBCARequestMock);
        Mockito.verify(snapAPI).paymentUsingKlikBCA(klikBCARequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void paymentUsingKlikBCAError_whenInvalidCertPath(){
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.paymentUsingSnapKlikBCA(snapAPI, klikBCARequestMock);
        Mockito.verify(snapAPI).paymentUsingKlikBCA(klikBCARequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    /**
     * Payment using BCA Klikpay
     */
    @Test
    public void paymentUsingBCAKlikpay_whenRequestNull() {
        eventBusImplementSample.paymentUsingSnapBCAKlikpay(snapAPI, null);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingBCAKlikpaySuccess_whenResponseNull() {
        eventBusImplementSample.paymentUsingSnapBCAKlikpay(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBCAKlikPay(BCAKlikpayRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingBCAKlikpaySuccess_whenCode200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("200");
        eventBusImplementSample.paymentUsingSnapBCAKlikpay(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingBCAKlikPay(BCAKlikpayRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionSuccessEvent();
    }

    @Test
    public void paymentUsingBCAKlikpaySuccess_whenCodeNot200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("300");
        eventBusImplementSample.paymentUsingSnapBCAKlikpay(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingBCAKlikPay(BCAKlikpayRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionFailedEvent();
    }

    @Test
    public void paymentUsingBCAKlikpayError_whenGeneralError() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.paymentUsingSnapBCAKlikpay(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingBCAKlikPay(BCAKlikpayRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingBCAKlikpayError_whenInvalidSSL() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.paymentUsingSnapBCAKlikpay(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingBCAKlikPay(BCAKlikpayRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void paymentUsingBCAKlikpayError_whenInvalidCertPath() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.paymentUsingSnapBCAKlikpay(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingBCAKlikPay(BCAKlikpayRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    /**
     * Payment using mandiri billpay
     */

    @Test
    public void paymentUsingMandiriBillpay_whenRequestNull() {
        eventBusImplementSample.paymentUsingSnapMandiriBillpay(snapAPI, null);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingMandiriBillpaySuccess_whenResponseNull() {
        eventBusImplementSample.paymentUsingSnapMandiriBillpay(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriBillPay(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingMandiriBillpay_whenCode200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("200");
        eventBusImplementSample.paymentUsingSnapMandiriBillpay(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriBillPay(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionSuccessEvent();
    }

    @Test
    public void paymentUsingMandirBillpaySuccess_whenCodeNot200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("300");
        eventBusImplementSample.paymentUsingSnapMandiriBillpay(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriBillPay(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionFailedEvent();
    }

    @Test
    public void paymentUsingMandiriBillPayError_whenGeneralError() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.paymentUsingSnapMandiriBillpay(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriBillPay(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingMandiriBillPayError_whenInvalidSSL() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.paymentUsingSnapMandiriBillpay(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriBillPay(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void paymentUsingMandiriBillpayError_whenInvalidCertPath() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.paymentUsingSnapMandiriBillpay(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriBillPay(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    /**
     * Payment using mandiri clickpay
     */

    @Test
    public void paymentUsingMandiriClickpaySuccess_whenResponseNull() {
        eventBusImplementSample.paymentUsingSnapMandiriClickPay(snapAPI, mandiriClickPayPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriClickPay(mandirClickPayPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingMandiriClickPaySuccess_whenCode200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("200");
        eventBusImplementSample.paymentUsingSnapMandiriClickPay(snapAPI, mandiriClickPayPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriClickPay(mandirClickPayPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionSuccessEvent();
    }

    @Test
    public void paymentUsingMandirClickPaySuccess_whenCodeNot200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("300");
        eventBusImplementSample.paymentUsingSnapMandiriClickPay(snapAPI, mandiriClickPayPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriClickPay(mandirClickPayPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionFailedEvent();
    }

    @Test
    public void paymentUsingMandiriClickPayError_whenGeneralError() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.paymentUsingSnapMandiriClickPay(snapAPI, mandiriClickPayPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriClickPay(mandirClickPayPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingMandiriClickpayError_whenInvalidSSL() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.paymentUsingSnapMandiriClickPay(snapAPI, mandiriClickPayPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriClickPay(mandirClickPayPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void paymentUsingMandiriClickPayError_whenInvalidCertPath() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.paymentUsingSnapMandiriClickPay(snapAPI, mandiriClickPayPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriClickPay(mandirClickPayPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    /**
     * Payment using cimbclick
     */

    @Test
    public void paymentCIMBClick_whenRequestNull() {
        eventBusImplementSample.paymentUsingSnapCIMBClick(snapAPI, null);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingCIMBClickpaySuccess_whenResponseNull() {
        eventBusImplementSample.paymentUsingSnapCIMBClick(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingCIMBClick(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingCIMBClickSuccess_whenCode200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("200");
        eventBusImplementSample.paymentUsingSnapCIMBClick(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingCIMBClick(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionSuccessEvent();
    }

    @Test
    public void paymentUsingCIMBClickSuccess_whenCodeNot200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("300");
        eventBusImplementSample.paymentUsingSnapCIMBClick(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingCIMBClick(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionFailedEvent();
    }

    @Test
    public void paymentUsingCIMBClickError_whenGeneralError() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.paymentUsingSnapCIMBClick(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingCIMBClick(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingCIMBClickError_whenInvalidSSL() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.paymentUsingSnapCIMBClick(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingCIMBClick(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void paymentUsingCIMBClickError_whenInvalidCertPath() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.paymentUsingSnapCIMBClick(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingCIMBClick(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    /**
     * Payment using bri epay
     */
    @Test
    public void paymentEpayBRI_whenRequestNull() {
        eventBusImplementSample.paymentUsingSnapBRIEpay(snapAPI, null);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingBRIEpaySuccess_whenResponseNull() {
        eventBusImplementSample.paymentUsingSnapBRIEpay(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingBRIEpay(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingBRIEpaySuccess_whenCode200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("200");
        eventBusImplementSample.paymentUsingSnapBRIEpay(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingBRIEpay(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionSuccessEvent();
    }

    @Test
    public void paymentUsingBRIEpayuccess_whenCodeNot200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("300");
        eventBusImplementSample.paymentUsingSnapBRIEpay(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingBRIEpay(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionFailedEvent();
    }

    @Test
    public void paymentUsingBRIEpayError_whenGeneralError() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.paymentUsingSnapBRIEpay(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingBRIEpay(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingBRIEpayError_whenInvalidSSL() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.paymentUsingSnapBRIEpay(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingBRIEpay(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void paymentUsingBRIEpayError_whenInvalidCertPath() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.paymentUsingSnapBRIEpay(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingBRIEpay(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    /**
     * Payment using mandiri ecash
     */

    @Test
    public void paymentTelkomselEcash_whenRequestNull() {
        eventBusImplementSample.paymentUsingSnapTelkomselEcash(snapAPI, null);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingMandiriEcashSuccess_whenResponseNull() {
        eventBusImplementSample.paymentUsingSnapMandirEcash(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriEcash(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingMandiriEcashSuccess_whenCode200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("200");
        eventBusImplementSample.paymentUsingSnapMandirEcash(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriEcash(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionSuccessEvent();
    }

    @Test
    public void paymentUsingMandiriEcashSuccess_whenCodeNot200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("300");
        eventBusImplementSample.paymentUsingSnapMandirEcash(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriEcash(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionFailedEvent();
    }

    @Test
    public void paymentUsingMandiriEcashError_whenGeneralError() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.paymentUsingSnapMandirEcash(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriEcash(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingMandiriEcashError_whenInvalidSSL() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.paymentUsingSnapMandirEcash(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriEcash(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void paymentUsingMandirEcashError_whenInvalidCertPath() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.paymentUsingSnapMandirEcash(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingMandiriEcash(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    /**
     * Payment using telkomsel ecash
     */

    @Test
    public void paymentUsingtelkomselEcash_whenRequestNull() {
        eventBusImplementSample.paymentUsingSnapTelkomselEcash(snapAPI, null);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingTelkomselEcashSuccess_whenResponseNull() {
        eventBusImplementSample.paymentUsingSnapTelkomselEcash(snapAPI, telkomselEcashPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingTelkomselEcash(telkomselEcashPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingTelkomselEcashSuccess_whenCode200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("200");
        eventBusImplementSample.paymentUsingSnapTelkomselEcash(snapAPI, telkomselEcashPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingTelkomselEcash(telkomselEcashPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionSuccessEvent();
    }

    @Test
    public void paymentUsingTelkomselEcashSuccess_whenCodeNot200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("300");
        eventBusImplementSample.paymentUsingSnapTelkomselEcash(snapAPI, telkomselEcashPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingTelkomselEcash(telkomselEcashPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionFailedEvent();
    }

    @Test
    public void paymentUsingTelkomselEcashError_whenGeneralError() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.paymentUsingSnapTelkomselEcash(snapAPI, telkomselEcashPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingTelkomselEcash(telkomselEcashPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingTelkomselEcashError_whenInvalidSSL() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.paymentUsingSnapTelkomselEcash(snapAPI, telkomselEcashPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingTelkomselEcash(telkomselEcashPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void paymentUsingTelkomselEcashError_whenInvalidCertPath() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.paymentUsingSnapTelkomselEcash(snapAPI, telkomselEcashPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingTelkomselEcash(telkomselEcashPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    /**
     * Payment using xl tunai
     */

    @Test
    public void paymentUsingXLTunai_whenRequestNull() {
        eventBusImplementSample.paymentUsingSnapXLTunai(snapAPI, null);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingXLTunaiSuccess_whenResponseNull() {
        eventBusImplementSample.paymentUsingSnapXLTunai(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingXlTunai(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingXLTunaiSuccess_whenCode200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("200");
        eventBusImplementSample.paymentUsingSnapXLTunai(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingXlTunai(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionSuccessEvent();
    }

    @Test
    public void paymentUsingXLTunaiSuccess_whenCodeNot200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("300");
        eventBusImplementSample.paymentUsingSnapXLTunai(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingXlTunai(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionFailedEvent();
    }

    @Test
    public void paymentUsingXLTunaiError_whenGeneralError() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.paymentUsingSnapXLTunai(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingXlTunai(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingXLTunaiError_whenInvalidSSL() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.paymentUsingSnapXLTunai(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingXlTunai(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void paymentUsingXLTunaiEcashError_whenInvalidCertPath() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.paymentUsingSnapXLTunai(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingXlTunai(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }


    /**
     * Payment using indomaret
     */

    @Test
    public void paymentUsingIndomaret_whenRequestNull() {
        eventBusImplementSample.paymentUsingSnapIndomaret(snapAPI, null);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingIndomaretuccess_whenResponseNull() {
        eventBusImplementSample.paymentUsingSnapIndomaret(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingIndomaret(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingIndomaretSuccess_whenCode200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("200");
        eventBusImplementSample.paymentUsingSnapIndomaret(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingIndomaret(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionSuccessEvent();
    }

    @Test
    public void paymentUsingIndomaretSuccess_whenCodeNot200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("300");
        eventBusImplementSample.paymentUsingSnapIndomaret(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingIndomaret(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionFailedEvent();
    }

    @Test
    public void paymentUsingIndomaretrror_whenGeneralError() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.paymentUsingSnapIndomaret(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingIndomaret(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingIndomaretError_whenInvalidSSL() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.paymentUsingSnapIndomaret(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingIndomaret(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void paymentUsingIndomaretEcashError_whenInvalidCertPath() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.paymentUsingSnapIndomaret(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingIndomaret(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    /**
     * Payment using indosat dompetku
     */
    @Test
    public void paymentUsingIndosatDompetku_whenRequestNull() {
        eventBusImplementSample.paymentUsingSnapIndosatDompetku(snapAPI, null);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }
    @Test
    public void paymentUsingIndosatDompetkuSuccess_whenResponseNull() {
        eventBusImplementSample.paymentUsingSnapIndosatDompetku(snapAPI, indosatDompetkuPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingIndosatDompetku(indosatDompetKuPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingIndosatDompetkuSuccess_whenCode200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("200");
        eventBusImplementSample.paymentUsingSnapIndosatDompetku(snapAPI, indosatDompetkuPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingIndosatDompetku(indosatDompetKuPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionSuccessEvent();
    }

    @Test
    public void paymetUsingIndosatDompetkuSuccess_whenCodeNot200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("300");
        eventBusImplementSample.paymentUsingSnapIndosatDompetku(snapAPI, indosatDompetkuPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingIndosatDompetku(indosatDompetKuPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionFailedEvent();
    }

    @Test
    public void paymentUsingIndosatDompetkuError_whenGeneralError() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.paymentUsingSnapIndosatDompetku(snapAPI, indosatDompetkuPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingIndosatDompetku(indosatDompetKuPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingIndosatDompetkuError_whenInvalidSSL() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.paymentUsingSnapIndosatDompetku(snapAPI, indosatDompetkuPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingIndosatDompetku(indosatDompetKuPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void paymentUsingIndosatDompetku_whenInvalidCertPath() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.paymentUsingSnapIndosatDompetku(snapAPI, indosatDompetkuPaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingIndosatDompetku(indosatDompetKuPaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    /**
     * Payment using kiosan
     */

    @Test
    public void paymentUsingKiosan_whenRequestNull() {
        eventBusImplementSample.paymentUsingSnapKiosan(snapAPI, null);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingKiosanSuccess_whenResponseNull() {
        eventBusImplementSample.paymentUsingSnapKiosan(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingKiosan(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingKiosanSuccess_whenCode200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("200");
        eventBusImplementSample.paymentUsingSnapKiosan(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingKiosan(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionSuccessEvent();
    }

    @Test
    public void paymetUsingKiosanSuccess_whenCodeNot200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("300");
        eventBusImplementSample.paymentUsingSnapKiosan(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingKiosan(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionFailedEvent();
    }

    @Test
    public void paymentUsingKiosanError_whenGeneralError() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.paymentUsingSnapKiosan(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingKiosan(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingKiosanError_whenInvalidSSL() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.paymentUsingSnapKiosan(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingKiosan(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void paymentUsingKiosanError_whenInvalidCertPath() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.paymentUsingSnapKiosan(snapAPI, basePaymentRequestMock);
        Mockito.verify(snapAPI).paymentUsingKiosan(basePaymentRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    /**
     * Payment using Bank Transfer All Bank.
     */

    @Test
    public void paymentUsingBankTransferAllBank_whenRequestNull() {
        eventBusImplementSample.paymentUsingSnapBankTransferAllBank(snapAPI, null);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }


    @Test
    public void paymentUsingBankTransferAllBankSuccess_whenResponseNull() {
        eventBusImplementSample.paymentUsingSnapBankTransferAllBank(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferAllBank(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(null, retrofitResponse);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingBankTransferAllBankSuccess_whenCode200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("200");
        eventBusImplementSample.paymentUsingSnapBankTransferAllBank(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferAllBank(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionSuccessEvent();
    }

    @Test
    public void paymentUsingBankTransferAllBankSuccess_whenCodeNot200() {
        Mockito.when(transactionResponseMock.getStatusCode()).thenReturn("300");
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.paymentUsingSnapBankTransferAllBank(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferAllBank(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().success(transactionResponseMock, retrofitResponse);
        Mockito.verify(busCollaborator).onTransactionFailedEvent();
    }

    @Test
    public void paymentUsingBankTransferAllBankError_whenGeneralError() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorGeneralMock);
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.paymentUsingSnapBankTransferAllBank(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferAllBank(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onGeneralErrorEvent();
    }

    @Test
    public void paymentUsingBankTransferAllBankError_whenInvalidSSL() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidSSLException);
        eventBusImplementSample.paymentUsingSnapBankTransferAllBank(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferAllBank(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

    @Test
    public void paymentUsingBankTransferAllBankError_whenInvalidCertPath() {
        Mockito.when(retrofitError.getCause()).thenReturn(errorInvalidCertPatMock);
        eventBusImplementSample.paymentUsingSnapBankTransferAllBank(snapAPI, bankTransferRequestMock);
        Mockito.verify(snapAPI).paymentUsingBankTransferAllBank(bankTransferRequestCaptor.capture(), transactionResponseCallbackCaptor.capture());
        transactionResponseCallbackCaptor.getValue().failure(retrofitError);
        Mockito.verify(busCollaborator).onSSLErrorEvent();
    }

}
