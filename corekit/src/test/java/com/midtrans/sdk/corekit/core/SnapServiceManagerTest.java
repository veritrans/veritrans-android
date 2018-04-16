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
import com.midtrans.sdk.corekit.models.snap.TransactionStatusResponse;
import com.midtrans.sdk.corekit.models.snap.params.CreditCardPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.GCIPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.IndosatDompetkuPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.KlikBcaPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.NewMandiriClickPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.TelkomselCashPaymentParams;
import com.midtrans.sdk.corekit.models.snap.payment.BankTransferPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.BasePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.CreditCardPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.CustomerDetailRequest;
import com.midtrans.sdk.corekit.models.snap.payment.DanamonOnlinePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GCIPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GoPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.KlikBCAPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.MandiriClickPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.NewMandiriClickPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.TelkomselEcashPaymentRequest;
import com.midtrans.sdk.corekit.utilities.CallbackCollaborator;
import com.midtrans.sdk.corekit.utilities.SnapServiceCallbackImplement;
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
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
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
public class SnapServiceManagerTest {

    private static final int ERR_TYPE_NPE = 1;
    @InjectMocks
    protected SnapServiceCallbackImplement callbackImplement;
    @Mock
    private Context contextMock;
    @Mock
    private Resources resourcesMock;
    private MidtransSDK midtransSDK;

    private String snapToken = "aa3afad7-a346-4db6-9cb3-737f24e4fc56";

    @Mock
    private CertPathValidatorException errorInvalidCertPatMock;
    @Mock
    private SSLHandshakeException errorInvalidSSLException;

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
    private Call<Transaction> callTransactionMock;
    @Mock
    private Call<TransactionResponse> callTransactionResponseMock;
    @Mock
    private Call<Void> callDeleteCardResponseMock;
    @Mock
    private Call<List<BankBinsResponse>> callBankBinsResponseMock;
    @Mock
    private Call<BanksPointResponse> callBankPointResponseMock;

    @Mock
    private Call<TransactionStatusResponse> callGetTransactionStatusMock;

    @Captor
    private ArgumentCaptor<Callback<Transaction>> transactionCaptor;
    @Mock
    private CallbackCollaborator callbackCollaboratorMock;

    @Captor
    private ArgumentCaptor<Callback<TransactionResponse>> transactionResponseCaptor;
    @Captor
    ArgumentCaptor<Callback<Void>> deleteCardResponseCaptor;
    @Captor
    ArgumentCaptor<Callback<List<BankBinsResponse>>> bankBinsResponseCaptor;
    @Captor
    ArgumentCaptor<Callback<BanksPointResponse>> bankPointsResponseCaptor;
    @Captor
    ArgumentCaptor<Callback<TransactionStatusResponse>> transactionStatusResponseCaptor;

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
        Mockito.when(MidtransRestAdapter.newSnapApiService(timout)).thenReturn(snapApiServiceMock);

        Mockito.when(SdkUtil.newPreferences(contextMock, "local.data")).thenReturn(preferencesMock);
        Mockito.when(SdkUtil.newSnapServiceManager(timout)).thenReturn(snapTransactionManager);

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
        Mockito.when(snapApiServiceMock.getPaymentOption(snapToken)).thenReturn(callTransactionMock);
        callbackImplement.getTransactionOptions(snapToken);
        Mockito.verify(callTransactionMock).enqueue(transactionCaptor.capture());
    }

    private Response<TransactionResponse> createResponseOfTransactionResponseSuccess(Integer statusCode, String paymentType, boolean emptyBody) {
        Request okReq = new Request.Builder().url(SDKConfigTest.SNAP_URL).build();
        okhttp3.Response okResponse = new okhttp3.Response.Builder().code((statusCode == null || statusCode > 300) ? 299 : statusCode).request(okReq).message("success").protocol(Protocol.HTTP_2).build();
        TransactionResponse transResponse = emptyBody ? null : createTransactionReponseSuccess(statusCode, paymentType);
        Response<TransactionResponse> resTransactionResponse = Response.success(transResponse, okResponse);
        return resTransactionResponse;
    }

    private TransactionResponse createTransactionReponseSuccess(Integer statusCode, String paymentType) {
        TransactionResponse response = new TransactionResponse("success");
        response.setPaymentType(paymentType);
        response.setStatusCode(String.valueOf(statusCode));
        return response;
    }

    private Response<Void> createCardDeletionReponseSuccess(Integer statusCode) {
        Request okReq = new Request.Builder().url(SDKConfigTest.SNAP_URL).build();
        okhttp3.Response okResponse = new okhttp3.Response.Builder().code((statusCode == null || statusCode > 300) ? 299 : statusCode).request(okReq).message("success").protocol(Protocol.HTTP_2).build();
        Void data = null;
        Response<Void> resTransactionResponse = Response.success(data, okResponse);
        return resTransactionResponse;
    }

    private Response<List<BankBinsResponse>> createBankBinsResponseSuccess(Integer statusCode) {
        Request okReq = new Request.Builder().url(SDKConfigTest.SNAP_URL).build();
        okhttp3.Response okResponse = new okhttp3.Response.Builder().code((statusCode == null || statusCode > 300) ? 299 : statusCode).request(okReq).message("success").protocol(Protocol.HTTP_2).build();

        BankBinsResponse bankBinsResponse = new BankBinsResponse();
        List<BankBinsResponse> bankBinsResponses = new ArrayList<>();
        bankBinsResponses.add(bankBinsResponse);

        Response<List<BankBinsResponse>> resTransactionResponse = Response.success(bankBinsResponses, okResponse);
        return resTransactionResponse;
    }

    private Response<BanksPointResponse> createBankPointResponse(Integer statusCode) {
        Request okReq = new Request.Builder().url(SDKConfigTest.SNAP_URL).build();
        okhttp3.Response okResponse = new okhttp3.Response.Builder().code((statusCode == null || statusCode > 300) ? 299 : statusCode).request(okReq).message("success").protocol(Protocol.HTTP_2).build();

        BanksPointResponse response = new BanksPointResponse(String.valueOf(statusCode), "point", null, 1000l, new Date().toString());

        Response<BanksPointResponse> resTransactionResponse = Response.success(response, okResponse);
        return resTransactionResponse;
    }

    private Response<TransactionStatusResponse> createTransactionStatusResponse(Integer statusCode, boolean bodyEmpty) {
        Request okReq = new Request.Builder().url(SDKConfigTest.SNAP_URL).build();
        okhttp3.Response okResponse = new okhttp3.Response.Builder().code((statusCode == null || statusCode > 300) ? 299 : statusCode).request(okReq).message("success").protocol(Protocol.HTTP_2).build();

        TransactionStatusResponse response = new TransactionStatusResponse();
        response.setStatusCode(String.valueOf(statusCode));

        Response<TransactionStatusResponse> resTransactionResponse = Response.success(bodyEmpty ? null: response, okResponse);
        return resTransactionResponse;
    }


    private Throwable createThrowableOfTransactionResponseFailure(int errorMessage) {
        switch (errorMessage) {
            case ERR_TYPE_NPE:
                return new Throwable(new NullPointerException());
        }

        return new Throwable();
    }

    private ArrayList<String> createValidationMessages() {
        List<String> validations = new ArrayList<>();
        validations.add("is paid");
        return new ArrayList<>(validations);
    }

    @Test
    public void getTransactionOptionSuccess() {
        initGetTransactions();
        transactionCaptor.getValue().onResponse(callTransactionMock, createResponseTransactionSuccess(200, false));
        Mockito.verify(callbackCollaboratorMock).onGetPaymentOptionSuccess();
    }

    @Test
    public void getTransactionOptionFailure_whenResponseCodeNot200() {
        initGetTransactions();
        transactionCaptor.getValue().onResponse(callTransactionMock, createResponseTransactionSuccess(234, false));
        Mockito.verify(callbackCollaboratorMock).onGetPaymentOptionFailure();
    }

    @Test
    public void getTransactionOptionFailure_whenResponsCodeNot200AndSnapTokenEmpty() {
        initGetTransactions();
        Response<Transaction> resTransaction = createResponseTransactionSuccess(234, false);
        Mockito.when(Mockito.spy(resTransaction.body()).getToken()).thenReturn(null);
        transactionCaptor.getValue().onResponse(callTransactionMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onGetPaymentOptionFailure();
    }

    @Test
    public void getTransactionOptionError_whenResponseCodeNotIn200To300() {
        initGetTransactions();
        transactionCaptor.getValue().onResponse(callTransactionMock, createResponseTransactionError(400));
        Mockito.verify(callbackCollaboratorMock).onError();
    }


    /**
     * credit card payment
     */

    private void initCreditCardpayment() {
        CreditCardPaymentRequest request = createCreditCardRequest();
        Mockito.when(snapApiServiceMock.paymentUsingCreditCard(snapToken, request)).thenReturn(callTransactionResponseMock);
        callbackImplement.paymentUsingCreditCard(snapToken, request);
        Mockito.verify(callTransactionResponseMock).enqueue(transactionResponseCaptor.capture());
    }

    private CreditCardPaymentRequest createCreditCardRequest() {
        CreditCardPaymentParams params = new CreditCardPaymentParams(SDKConfigTest.CARD_TOKEN, false, SDKConfigTest.MASKED_CARD_NUMBER);
        CustomerDetailRequest cdRequest = new CustomerDetailRequest();
        CreditCardPaymentRequest creditCardPaymentRequest = new CreditCardPaymentRequest(PaymentType.CREDIT_CARD, params, cdRequest);
        return creditCardPaymentRequest;
    }

    @Test
    public void paymentUsingCreditCardSuccess() {
        initCreditCardpayment();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(200, PaymentType.CREDIT_CARD, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionSuccess();
    }


    @Test
    public void paymentUsingCreditCardFailure_whenEmptyStatusCode() {
        initCreditCardpayment();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(null, PaymentType.CREDIT_CARD, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingCreditCardFailure_whenStatusCodeNot200Or201() {
        initCreditCardpayment();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(204, PaymentType.CREDIT_CARD, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingCreditCardFailure_whenStatusCode400() {
        initCreditCardpayment();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(400, PaymentType.CREDIT_CARD, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingCreditCardFailure_whenValidationMessagesNullOrEmpty() {
        initCreditCardpayment();
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.CREDIT_CARD, false);

        //Validation Messages Null
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(null);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();

        //Validation Messages Empty
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(new ArrayList<String>());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock, Mockito.times(2)).onTransactionFailure();
    }

    @Test
    public void paymentUsingCreditCardFailure_whenValidationMessagesNotEmpty() {
        initCreditCardpayment();
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.CREDIT_CARD, false);
        resTransaction.body().setValidationMessages(createValidationMessages());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingCreditCardFailure_whenServiceNull() {
        snapTransactionManager.setService(null);
        callbackImplement.paymentUsingCreditCard(snapToken, null);
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingCreditCardError_whenEmptyResponseBody() {
        initCreditCardpayment();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(200, PaymentType.CREDIT_CARD, true));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingCreditCardError() {
        initCreditCardpayment();
        transactionResponseCaptor.getValue().onFailure(callTransactionResponseMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    /**
     * payment using va
     */

    private void initVaRequest(String paymentType) {
        BankTransferPaymentRequest request = createVaRequest(paymentType);
        Mockito.when(snapApiServiceMock.paymentUsingVa(snapToken, request)).thenReturn(callTransactionResponseMock);
        callbackImplement.paymentUsingVa(snapToken, request);
        Mockito.verify(callTransactionResponseMock).enqueue(transactionResponseCaptor.capture());
    }

    private BankTransferPaymentRequest createVaRequest(String paymentType) {
        BankTransferPaymentRequest request = new BankTransferPaymentRequest(paymentType, new CustomerDetailRequest());
        return request;
    }

    @Test
    public void paymentUsingVaSuccess() {
        initVaRequest(PaymentType.ALL_VA);
        Response<TransactionResponse> response = createResponseOfTransactionResponseSuccess(200, PaymentType.ALL_VA, false);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onTransactionSuccess();
        Assert.assertEquals(PaymentType.ALL_VA, response.body().getPaymentType());
    }


    @Test
    public void paymentUsingVaFailure_whenEmptyStatusCode() {
        initVaRequest(PaymentType.ALL_VA);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(null, PaymentType.BNI_VA, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();

    }

    @Test
    public void paymentUsingVaFailure_whenStatusCodeNot200Or201() {
        initVaRequest(PaymentType.ALL_VA);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(204, PaymentType.ALL_VA, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingVaFailure_whenStatusCode400() {
        initVaRequest(PaymentType.ALL_VA);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(400, PaymentType.ALL_VA, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingVaFailure_whenValidationMessagesNullOrEmpty() {
        initVaRequest(PaymentType.ALL_VA);
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.ALL_VA, false);

        //Validation Messages Null
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(null);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();

        //Validation Messages Empty
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(new ArrayList<String>());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock, Mockito.times(2)).onTransactionFailure();
    }

    @Test
    public void paymentUsingVaFailure_whenValidationMessagesNotEmpty() {
        initVaRequest(PaymentType.ALL_VA);
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.ALL_VA, false);
        resTransaction.body().setValidationMessages(createValidationMessages());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingVaFailure_whenServiceNull() {
        snapTransactionManager.setService(null);
        callbackImplement.paymentUsingVa(snapToken, null);
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingVaError_whenEmptyResponseBody() {
        initVaRequest(PaymentType.ALL_VA);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(200, PaymentType.ALL_VA, true));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingVaError() {
        initVaRequest(PaymentType.ALL_VA);
        transactionResponseCaptor.getValue().onFailure(callTransactionResponseMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    /**
     * payment using base method
     */

    private void initBaseMethod(String paymentType) {
        BasePaymentRequest request = createBaseRequest(paymentType);
        Mockito.when(snapApiServiceMock.paymentUsingBaseMethod(snapToken, request)).thenReturn(callTransactionResponseMock);
        callbackImplement.paymentBaseMethod(snapToken, request);
        Mockito.verify(callTransactionResponseMock).enqueue(transactionResponseCaptor.capture());
    }

    private BasePaymentRequest createBaseRequest(String paymentType) {
        BasePaymentRequest request = new BasePaymentRequest(paymentType);
        return request;
    }

    @Test
    public void paymentUsingBaseMethodSuccess() {
        initBaseMethod(PaymentType.DANAMON_ONLINE);
        Response<TransactionResponse> response = createResponseOfTransactionResponseSuccess(200, PaymentType.DANAMON_ONLINE, false);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onTransactionSuccess();
        Assert.assertEquals(PaymentType.DANAMON_ONLINE, response.body().getPaymentType());
    }


    @Test
    public void paymentUsingBaseMethod_whenEmptyStatusCode() {
        initBaseMethod(PaymentType.DANAMON_ONLINE);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(null, PaymentType.DANAMON_ONLINE, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingBaseMethodFailure_whenStatusCodeNot200Or201() {
        initBaseMethod(PaymentType.DANAMON_ONLINE);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(204, PaymentType.DANAMON_ONLINE, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingBaseMethodFailure_whenStatusCode400() {
        initBaseMethod(PaymentType.DANAMON_ONLINE);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(400, PaymentType.DANAMON_ONLINE, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingBaseMethodFailure_whenValidationMessagesNullOrEmpty() {
        initBaseMethod(PaymentType.DANAMON_ONLINE);
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.DANAMON_ONLINE, false);

        //Validation Messages Null
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(null);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();

        //Validation Messages Empty
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(new ArrayList<String>());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock, Mockito.times(2)).onTransactionFailure();
    }

    @Test
    public void paymentUsingBaseMethodFailure_whenValidationMessagesNotEmpty() {
        initBaseMethod(PaymentType.DANAMON_ONLINE);
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.DANAMON_ONLINE, false);
        resTransaction.body().setValidationMessages(createValidationMessages());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingBaseMethodFailure_whenServiceNull() {
        snapTransactionManager.setService(null);
        callbackImplement.paymentBaseMethod(snapToken, null);
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingBaseMethodError_whenEmptyResponseBody() {
        initBaseMethod(PaymentType.DANAMON_ONLINE);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(200, PaymentType.DANAMON_ONLINE, true));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingBaseMethodError() {
        initBaseMethod(PaymentType.DANAMON_ONLINE);
        transactionResponseCaptor.getValue().onFailure(callTransactionResponseMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    /**
     * payment using mandiri click pay
     */

    private void initMandiriClickPay() {
        NewMandiriClickPayPaymentRequest request = createMandiriRequest();
        Mockito.when(snapApiServiceMock.paymentUsingMandiriClickPay(snapToken, request)).thenReturn(callTransactionResponseMock);
        callbackImplement.paymentUsingMandiriClickPay(snapToken, request);
        Mockito.verify(callTransactionResponseMock).enqueue(transactionResponseCaptor.capture());
    }

    private NewMandiriClickPayPaymentRequest createMandiriRequest() {
        String mandiriCardNumber = "4111111111111111";
        String input3 = "123123";
        String token = "token1321";

        NewMandiriClickPaymentParams params = new NewMandiriClickPaymentParams(mandiriCardNumber, input3, token);
        NewMandiriClickPayPaymentRequest request = new NewMandiriClickPayPaymentRequest(PaymentType.MANDIRI_CLICKPAY, params);
        return request;
    }

    @Test
    public void paymentUsingMandiriClickPaySuccess() {
        initMandiriClickPay();
        Response<TransactionResponse> response = createResponseOfTransactionResponseSuccess(200, PaymentType.MANDIRI_CLICKPAY, false);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onTransactionSuccess();
        Assert.assertEquals(PaymentType.MANDIRI_CLICKPAY, response.body().getPaymentType());
    }


    @Test
    public void paymentUsingMandiriClickPay_whenEmptyStatusCode() {
        initMandiriClickPay();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(null, PaymentType.MANDIRI_CLICKPAY, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingMandiriClickPayFailure_whenStatusCodeNot200Or201() {
        initMandiriClickPay();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(204, PaymentType.MANDIRI_CLICKPAY, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingMandiriClickPayFailure_whenStatusCode400() {
        initMandiriClickPay();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(400, PaymentType.MANDIRI_CLICKPAY, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingMandiriClickPayFailure_whenValidationMessagesNullOrEmpty() {
        initMandiriClickPay();
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.MANDIRI_CLICKPAY, false);

        //Validation Messages Null
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(null);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();

        //Validation Messages Empty
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(new ArrayList<String>());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock, Mockito.times(2)).onTransactionFailure();
    }

    @Test
    public void paymentUsingMandiriClickPayFailure_whenValidationMessagesNotEmpty() {
        initMandiriClickPay();
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.MANDIRI_CLICKPAY, false);
        resTransaction.body().setValidationMessages(createValidationMessages());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingMandiriClickPayFailure_whenServiceNull() {
        snapTransactionManager.setService(null);
        callbackImplement.paymentBaseMethod(snapToken, null);
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingMandiriClickPayError_whenEmptyResponseBody() {
        initMandiriClickPay();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(200, PaymentType.MANDIRI_CLICKPAY, true));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingMandiriClickPayError() {
        initMandiriClickPay();
        transactionResponseCaptor.getValue().onFailure(callTransactionResponseMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    /**
     * payment using T-Cash
     */

    private void initTCash() {
        TelkomselEcashPaymentRequest request = createTCashRequest();
        Mockito.when(snapApiServiceMock.paymentUsingTelkomselEcash(snapToken, request)).thenReturn(callTransactionResponseMock);
        callbackImplement.paymentUsingTCash(snapToken, request);
        Mockito.verify(callTransactionResponseMock).enqueue(transactionResponseCaptor.capture());
    }

    private TelkomselEcashPaymentRequest createTCashRequest() {
        TelkomselCashPaymentParams params = new TelkomselCashPaymentParams("08123456789");
        TelkomselEcashPaymentRequest request = new TelkomselEcashPaymentRequest(PaymentType.TELKOMSEL_CASH, params);
        return request;
    }

    @Test
    public void paymentUsingTCashSuccess() {
        initTCash();
        Response<TransactionResponse> response = createResponseOfTransactionResponseSuccess(200, PaymentType.TELKOMSEL_CASH, false);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onTransactionSuccess();
        Assert.assertEquals(PaymentType.TELKOMSEL_CASH, response.body().getPaymentType());
    }


    @Test
    public void paymentUsingTCash_whenEmptyStatusCode() {
        initTCash();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(null, PaymentType.TELKOMSEL_CASH, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingTCashFailure_whenStatusCodeNot200Or201() {
        initTCash();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(204, PaymentType.TELKOMSEL_CASH, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingTCashFailure_whenStatusCode400() {
        initTCash();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(400, PaymentType.TELKOMSEL_CASH, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingTCashFailure_whenValidationMessagesNullOrEmpty() {
        initTCash();
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.TELKOMSEL_CASH, false);

        //Validation Messages Null
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(null);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();

        //Validation Messages Empty
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(new ArrayList<String>());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock, Mockito.times(2)).onTransactionFailure();
    }

    @Test
    public void paymentUsingTCashFailure_whenValidationMessagesNotEmpty() {
        initTCash();
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.TELKOMSEL_CASH, false);
        resTransaction.body().setValidationMessages(createValidationMessages());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingTCashFailure_whenServiceNull() {
        snapTransactionManager.setService(null);
        callbackImplement.paymentUsingTCash(snapToken, null);
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingTCashError_whenEmptyResponseBody() {
        initTCash();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(200, PaymentType.TELKOMSEL_CASH, true));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingTCashError() {
        initTCash();
        transactionResponseCaptor.getValue().onFailure(callTransactionResponseMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    /**
     * payment using Indosat Dompetku
     */

    private void initIndosatDompetKu() {
        IndosatDompetkuPaymentRequest request = createIndosatDompetkuRequest();
        Mockito.when(snapApiServiceMock.paymentUsingIndosatDompetku(snapToken, request)).thenReturn(callTransactionResponseMock);
        callbackImplement.paymentUsingIndosatDomputku(snapToken, request);
        Mockito.verify(callTransactionResponseMock).enqueue(transactionResponseCaptor.capture());
    }

    private IndosatDompetkuPaymentRequest createIndosatDompetkuRequest() {
        IndosatDompetkuPaymentParams params = new IndosatDompetkuPaymentParams("msisdn");
        IndosatDompetkuPaymentRequest request = new IndosatDompetkuPaymentRequest(PaymentType.INDOSAT_DOMPETKU, params);
        return request;
    }

    @Test
    public void paymentUsingIndosatDompetkuSuccess() {
        initIndosatDompetKu();
        Response<TransactionResponse> response = createResponseOfTransactionResponseSuccess(200, PaymentType.INDOSAT_DOMPETKU, false);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onTransactionSuccess();
        Assert.assertEquals(PaymentType.INDOSAT_DOMPETKU, response.body().getPaymentType());
    }


    @Test
    public void paymentUsingIndosatDompetku_whenEmptyStatusCode() {
        initIndosatDompetKu();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(null, PaymentType.INDOSAT_DOMPETKU, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingIndosatDompetkuFailure_whenStatusCodeNot200Or201() {
        initIndosatDompetKu();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(204, PaymentType.INDOSAT_DOMPETKU, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingIndosatDompetkuFailure_whenStatusCode400() {
        initIndosatDompetKu();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(400, PaymentType.INDOSAT_DOMPETKU, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingIndosatDompetkuFailure_whenValidationMessagesNullOrEmpty() {
        initIndosatDompetKu();
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.INDOSAT_DOMPETKU, false);

        //Validation Messages Null
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(null);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();

        //Validation Messages Empty
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(new ArrayList<String>());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock, Mockito.times(2)).onTransactionFailure();
    }

    @Test
    public void paymentUsingIndosatDompetkuFailure_whenValidationMessagesNotEmpty() {
        initIndosatDompetKu();
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.INDOSAT_DOMPETKU, false);
        resTransaction.body().setValidationMessages(createValidationMessages());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingIndosatDompetkuFailure_whenServiceNull() {
        snapTransactionManager.setService(null);
        callbackImplement.paymentUsingIndosatDomputku(snapToken, null);
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingIndosatDompetkuError_whenEmptyResponseBody() {
        initIndosatDompetKu();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(200, PaymentType.INDOSAT_DOMPETKU, true));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingIndosatDompetkuError() {
        initIndosatDompetKu();
        transactionResponseCaptor.getValue().onFailure(callTransactionResponseMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    /**
     * payment using GCI
     */

    private void initGci() {
        GCIPaymentRequest request = createGciRequest();
        Mockito.when(snapApiServiceMock.paymentUsingGci(snapToken, request)).thenReturn(callTransactionResponseMock);
        callbackImplement.paymentUsingGci(snapToken, request);
        Mockito.verify(callTransactionResponseMock).enqueue(transactionResponseCaptor.capture());
    }

    private GCIPaymentRequest createGciRequest() {
        GCIPaymentParams params = new GCIPaymentParams(SDKConfigTest.CARD_NUMBER, "pass1234");
        GCIPaymentRequest request = new GCIPaymentRequest(params, PaymentType.GCI);
        return request;
    }

    @Test
    public void paymentUsingGciSuccess() {
        initGci();
        Response<TransactionResponse> response = createResponseOfTransactionResponseSuccess(200, PaymentType.GCI, false);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onTransactionSuccess();
        Assert.assertEquals(PaymentType.GCI, response.body().getPaymentType());
    }


    @Test
    public void paymentUsingGci_whenEmptyStatusCode() {
        initGci();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(null, PaymentType.GCI, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingGciFailure_whenStatusCodeNot200Or201() {
        initGci();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(204, PaymentType.GCI, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingGciFailure_whenStatusCode400() {
        initGci();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(400, PaymentType.GCI, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingGciFailure_whenValidationMessagesNullOrEmpty() {
        initGci();
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.GCI, false);

        //Validation Messages Null
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(null);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();

        //Validation Messages Empty
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(new ArrayList<String>());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock, Mockito.times(2)).onTransactionFailure();
    }

    @Test
    public void paymentUsingGciFailure_whenValidationMessagesNotEmpty() {
        initGci();
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.GCI, false);
        resTransaction.body().setValidationMessages(createValidationMessages());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingGciFailure_whenServiceNull() {
        snapTransactionManager.setService(null);
        callbackImplement.paymentUsingGci(snapToken, null);
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingGciError_whenEmptyResponseBody() {
        initGci();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(200, PaymentType.GCI, true));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingGciError() {
        initTCash();
        transactionResponseCaptor.getValue().onFailure(callTransactionResponseMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    /**
     * payment using KlikBca
     */

    private void initKlikBca() {
        KlikBCAPaymentRequest request = createKlikBcaRequesst();
        Mockito.when(snapApiServiceMock.paymentUsingKlikBca(snapToken, request)).thenReturn(callTransactionResponseMock);
        callbackImplement.paymentUsingKlikBca(snapToken, request);
        Mockito.verify(callTransactionResponseMock).enqueue(transactionResponseCaptor.capture());
    }

    private KlikBCAPaymentRequest createKlikBcaRequesst() {
        KlikBcaPaymentParams params = new KlikBcaPaymentParams("user_id");
        KlikBCAPaymentRequest request = new KlikBCAPaymentRequest(PaymentType.KLIK_BCA, params);
        return request;
    }

    @Test
    public void paymentUsingKlikBcaSuccess() {
        initKlikBca();
        Response<TransactionResponse> response = createResponseOfTransactionResponseSuccess(200, PaymentType.KLIK_BCA, false);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onTransactionSuccess();
        Assert.assertEquals(PaymentType.KLIK_BCA, response.body().getPaymentType());
    }


    @Test
    public void paymentUsingKlikBca_whenEmptyStatusCode() {
        initKlikBca();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(null, PaymentType.KLIK_BCA, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingKlikBcaFailure_whenStatusCodeNot200Or201() {
        initKlikBca();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(204, PaymentType.KLIK_BCA, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingKlikBcaFailure_whenStatusCode400() {
        initKlikBca();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(400, PaymentType.KLIK_BCA, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingKlikBcaFailure_whenValidationMessagesNullOrEmpty() {
        initKlikBca();
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.KLIK_BCA, false);

        //Validation Messages Null
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(null);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();

        //Validation Messages Empty
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(new ArrayList<String>());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock, Mockito.times(2)).onTransactionFailure();
    }

    @Test
    public void paymentUsingKlikBcaFailure_whenValidationMessagesNotEmpty() {
        initKlikBca();
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.KLIK_BCA, false);
        resTransaction.body().setValidationMessages(createValidationMessages());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingKlikBcaFailure_whenServiceNull() {
        snapTransactionManager.setService(null);
        callbackImplement.paymentUsingKlikBca(snapToken, null);
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingKlikBcaError_whenEmptyResponseBody() {
        initKlikBca();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(200, PaymentType.KLIK_BCA, true));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingKlikBcaError() {
        initTCash();
        transactionResponseCaptor.getValue().onFailure(callTransactionResponseMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }


    /**
     * payment using GoPay
     */

    private void initGoPay() {
        GoPayPaymentRequest request = createGoPayPaymentRequest();
        Mockito.when(snapApiServiceMock.paymentUsingGoPay(snapToken, request)).thenReturn(callTransactionResponseMock);
        callbackImplement.paymentUsingGopay(snapToken, request);
        Mockito.verify(callTransactionResponseMock).enqueue(transactionResponseCaptor.capture());
    }

    private GoPayPaymentRequest createGoPayPaymentRequest() {
        GoPayPaymentRequest request = new GoPayPaymentRequest(PaymentType.GOPAY);
        return request;
    }

    @Test
    public void paymentUsingGoPaySuccess() {
        initGoPay();
        Response<TransactionResponse> response = createResponseOfTransactionResponseSuccess(200, PaymentType.GOPAY, false);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onTransactionSuccess();
        Assert.assertEquals(PaymentType.GOPAY, response.body().getPaymentType());
    }


    @Test
    public void paymentUsingGoPay_whenEmptyStatusCode() {
        initGoPay();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(null, PaymentType.GOPAY, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingGoPayFailure_whenStatusCodeNot200Or201() {
        initGoPay();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(204, PaymentType.GOPAY, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingGoPayFailure_whenStatusCode400() {
        initGoPay();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(400, PaymentType.GOPAY, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingGoPayFailure_whenValidationMessagesNullOrEmpty() {
        initGoPay();
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.GOPAY, false);

        //Validation Messages Null
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(null);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();

        //Validation Messages Empty
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(new ArrayList<String>());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock, Mockito.times(2)).onTransactionFailure();
    }

    @Test
    public void paymentUsingGoPayFailure_whenValidationMessagesNotEmpty() {
        initGoPay();
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.GOPAY, false);
        resTransaction.body().setValidationMessages(createValidationMessages());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingGoPayFailure_whenServiceNull() {
        snapTransactionManager.setService(null);
        callbackImplement.paymentUsingGopay(snapToken, null);
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingGoPayError_whenEmptyResponseBody() {
        initGoPay();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(200, PaymentType.GOPAY, true));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingGoPayError() {
        initGoPay();
        transactionResponseCaptor.getValue().onFailure(callTransactionResponseMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    /**
     * payment using danamon online
     */

    private void initDanamonOnline() {
        DanamonOnlinePaymentRequest request = createDanamonOnlineRequest();
        Mockito.when(snapApiServiceMock.paymentUsingDanamonOnline(snapToken, request)).thenReturn(callTransactionResponseMock);
        callbackImplement.paymentUsingDanamonOnline(snapToken, request);
        Mockito.verify(callTransactionResponseMock).enqueue(transactionResponseCaptor.capture());
    }

    private DanamonOnlinePaymentRequest createDanamonOnlineRequest() {
        DanamonOnlinePaymentRequest request = new DanamonOnlinePaymentRequest(PaymentType.DANAMON_ONLINE);
        return request;
    }

    @Test
    public void paymentUsingdanamonOnlineSuccess() {
        initDanamonOnline();
        Response<TransactionResponse> response = createResponseOfTransactionResponseSuccess(200, PaymentType.DANAMON_ONLINE, false);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onTransactionSuccess();
        Assert.assertEquals(PaymentType.DANAMON_ONLINE, response.body().getPaymentType());
    }


    @Test
    public void paymentUsingdanamonOnline_whenEmptyStatusCode() {
        initDanamonOnline();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(null, PaymentType.DANAMON_ONLINE, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingdanamonOnlineFailure_whenStatusCodeNot200Or201() {
        initDanamonOnline();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(204, PaymentType.DANAMON_ONLINE, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingdanamonOnlineFailure_whenStatusCode400() {
        initDanamonOnline();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(400, PaymentType.DANAMON_ONLINE, false));
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingdanamonOnlineFailure_whenValidationMessagesNullOrEmpty() {
        initDanamonOnline();
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.DANAMON_ONLINE, false);

        //Validation Messages Null
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(null);
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();

        //Validation Messages Empty
        Mockito.when(Mockito.spy(resTransaction.body()).getValidationMessages()).thenReturn(new ArrayList<String>());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock, Mockito.times(2)).onTransactionFailure();
    }

    @Test
    public void paymentUsingdanamonOnlineFailure_whenValidationMessagesNotEmpty() {
        initDanamonOnline();
        Response<TransactionResponse> resTransaction = createResponseOfTransactionResponseSuccess(400, PaymentType.DANAMON_ONLINE, false);
        resTransaction.body().setValidationMessages(createValidationMessages());
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, resTransaction);
        Mockito.verify(callbackCollaboratorMock).onTransactionFailure();
    }

    @Test
    public void paymentUsingdanamonOnlineFailure_whenServiceNull() {
        snapTransactionManager.setService(null);
        callbackImplement.paymentUsingDanamonOnline(snapToken, null);
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingdanamonOnlineError_whenEmptyResponseBody() {
        initDanamonOnline();
        transactionResponseCaptor.getValue().onResponse(callTransactionResponseMock, createResponseOfTransactionResponseSuccess(200, PaymentType.DANAMON_ONLINE, true));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void paymentUsingDanamonOnlineError() {
        initDanamonOnline();
        transactionResponseCaptor.getValue().onFailure(callTransactionResponseMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }


    /**
     * delete saved cards
     */

    private void initDeleteCard() {
        Mockito.when(snapApiServiceMock.deleteCard(snapToken, SDKConfigTest.MASKED_CARD_NUMBER)).thenReturn(callDeleteCardResponseMock);
        callbackImplement.deleteSavedCard(snapToken, SDKConfigTest.MASKED_CARD_NUMBER);
        Mockito.verify(callDeleteCardResponseMock).enqueue(deleteCardResponseCaptor.capture());
    }

    @Test
    public void cardDeletionSuccess() {
        initDeleteCard();
        Response<Void> response = createCardDeletionReponseSuccess(200);
        deleteCardResponseCaptor.getValue().onResponse(callDeleteCardResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onDeleteCardSuccess();
    }


    @Test
    public void cardDeletionFailure_whenEmptyStatusCode() {
        initDeleteCard();
        Response<Void> response = createCardDeletionReponseSuccess(null);
        deleteCardResponseCaptor.getValue().onResponse(callDeleteCardResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onDeleteCardFailure();
    }

    @Test
    public void cardDeletionFailure_whenStatusCodeNot200Or201() {
        initDeleteCard();
        Response<Void> response = createCardDeletionReponseSuccess(204);
        deleteCardResponseCaptor.getValue().onResponse(callDeleteCardResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onDeleteCardFailure();
    }


    @Test
    public void cardDeletionFailure_whenServiceNull() {
        snapTransactionManager.setService(null);
        callbackImplement.deleteSavedCard(snapToken, null);
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void cardDeletionError() {
        initDeleteCard();
        deleteCardResponseCaptor.getValue().onFailure(callDeleteCardResponseMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }


    /**
     * get bank bins
     */

    private void initGetBankBins() {
        Mockito.when(snapApiServiceMock.getBankBins()).thenReturn(callBankBinsResponseMock);
        callbackImplement.getBankBins();
        Mockito.verify(callBankBinsResponseMock).enqueue(bankBinsResponseCaptor.capture());
    }

    @Test
    public void getBankBinsSuccess() {
        initGetBankBins();
        Response<List<BankBinsResponse>> response = createBankBinsResponseSuccess(200);
        bankBinsResponseCaptor.getValue().onResponse(callBankBinsResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onGetBankBinSuccess();
    }


    @Test
    public void getBankBinsFailure_whenEmptyStatusCode() {
        initGetBankBins();
        Response<List<BankBinsResponse>> response = createBankBinsResponseSuccess(null);
        bankBinsResponseCaptor.getValue().onResponse(callBankBinsResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onGetBankBinFailure();
    }

    @Test
    public void getBankBinsFailure_whenStatusCodeNot200Or201() {
        initGetBankBins();
        Response<List<BankBinsResponse>> response = createBankBinsResponseSuccess(204);
        bankBinsResponseCaptor.getValue().onResponse(callBankBinsResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onGetBankBinFailure();
    }


    @Test
    public void getBankBinsFailure_whenServiceNull() {
        snapTransactionManager.setService(null);
        callbackImplement.getBankBins();
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void getBankBinsError() {
        initGetBankBins();
        bankBinsResponseCaptor.getValue().onFailure(callBankBinsResponseMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }


    /**
     * get bank points
     */

    private void initGetBankPoints() {
        Mockito.when(snapApiServiceMock.getBanksPoint(snapToken, SDKConfigTest.CARD_TOKEN)).thenReturn(callBankPointResponseMock);
        callbackImplement.getBankPoints(snapToken, SDKConfigTest.CARD_TOKEN);
        Mockito.verify(callBankPointResponseMock).enqueue(bankPointsResponseCaptor.capture());
    }

    @Test
    public void getBankPointsSuccess() {
        initGetBankPoints();
        Response<BanksPointResponse> response = createBankPointResponse(200);
        bankPointsResponseCaptor.getValue().onResponse(callBankPointResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onGetbanksPointSuccess();
    }


    @Test
    public void getBankPointsFailure_whenEmptyStatusCode() {
        initGetBankPoints();
        Response<BanksPointResponse> response = createBankPointResponse(null);
        bankPointsResponseCaptor.getValue().onResponse(callBankPointResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onGetBankBinFailure();
    }

    @Test
    public void getBankPointsFailure_whenStatusCodeNot200Or201() {
        initGetBankPoints();
        Response<BanksPointResponse> response = createBankPointResponse(204);
        bankPointsResponseCaptor.getValue().onResponse(callBankPointResponseMock, response);
        Mockito.verify(callbackCollaboratorMock).onGetBankBinFailure();
    }


    @Test
    public void getBankPointsFailure_whenServiceNull() {
        snapTransactionManager.setService(null);
        callbackImplement.getBankPoints(snapToken, SDKConfigTest.CARD_TOKEN);
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void getBankPointsError() {
        initGetBankPoints();
        bankPointsResponseCaptor.getValue().onFailure(callBankPointResponseMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }


    /**
     * get Transaction Status
     */

    private void initGetTransactionStatus() {
        Mockito.when(snapApiServiceMock.getTransactionStatus(snapToken)).thenReturn(callGetTransactionStatusMock);
        callbackImplement.getTransactionStatus(snapToken);
        Mockito.verify(callGetTransactionStatusMock).enqueue(transactionStatusResponseCaptor.capture());
    }

    @Test
    public void gettransactionStatusSuccess() {
        initGetTransactionStatus();
        Response<TransactionStatusResponse> response = createTransactionStatusResponse(200, false);
        transactionStatusResponseCaptor.getValue().onResponse(callGetTransactionStatusMock, response);
        Mockito.verify(callbackCollaboratorMock).onGetTransactionStatatusSuccess();
    }


    @Test
    public void gettransactionStatusFailure_whenEmptyStatusCode() {
        initGetTransactionStatus();
        Response<TransactionStatusResponse> response = createTransactionStatusResponse(null, false);
        transactionStatusResponseCaptor.getValue().onResponse(callGetTransactionStatusMock, response);
        Mockito.verify(callbackCollaboratorMock).onGetTransactionStatatusFailure();
    }

    @Test
    public void gettransactionStatusFailure_whenStatusCodeNot200Or201() {
        initGetTransactionStatus();
        Response<TransactionStatusResponse> response = createTransactionStatusResponse(204, false);
        transactionStatusResponseCaptor.getValue().onResponse(callGetTransactionStatusMock, response);
        Mockito.verify(callbackCollaboratorMock).onGetTransactionStatatusFailure();
    }


    @Test
    public void gettransactionStatusFailure_whenServiceNull() {
        snapTransactionManager.setService(null);
        callbackImplement.getTransactionOptions(snapToken);
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void gettransactionStatusError() {
        initGetTransactionStatus();
        transactionStatusResponseCaptor.getValue().onFailure(callGetTransactionStatusMock, createThrowableOfTransactionResponseFailure(ERR_TYPE_NPE));
        Mockito.verify(callbackCollaboratorMock).onError();
    }

    @Test
    public void gettransactionStatusError_whenEmptyResponseBody() {
        initGetTransactionStatus();
        transactionStatusResponseCaptor.getValue().onResponse(callGetTransactionStatusMock, createTransactionStatusResponse(345, true));
        Mockito.verify(callbackCollaboratorMock).onError();
    }
}
