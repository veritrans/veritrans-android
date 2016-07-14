package id.co.veritrans.sdk.coreflow.core;

import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import id.co.veritrans.sdk.coreflow.R;
import id.co.veritrans.sdk.coreflow.models.BBMMoneyRequestModel;
import id.co.veritrans.sdk.coreflow.models.BCABankTransfer;
import id.co.veritrans.sdk.coreflow.models.BCAKlikPayModel;
import id.co.veritrans.sdk.coreflow.models.CIMBClickPayModel;
import id.co.veritrans.sdk.coreflow.models.CardTransfer;
import id.co.veritrans.sdk.coreflow.models.EpayBriTransfer;
import id.co.veritrans.sdk.coreflow.models.IndomaretRequestModel;
import id.co.veritrans.sdk.coreflow.models.IndosatDompetkuRequest;
import id.co.veritrans.sdk.coreflow.models.KlikBCAModel;
import id.co.veritrans.sdk.coreflow.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.coreflow.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.coreflow.models.MandiriECashModel;
import id.co.veritrans.sdk.coreflow.models.PermataBankTransfer;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.restapi.RestAPIMocUtilites;
import id.co.veritrans.sdk.coreflow.transactionmanager.TransactionMangerMain;
import retrofit.Callback;

/**
 * Created by ziahaqi on 01/07/2016.
 */
public class TransactionManagerPaymentTest extends TransactionMangerMain {
    /*
     * bbm money
     */
    @Captor
    private ArgumentCaptor<String> xauthCaptor;
    @Captor
    private ArgumentCaptor<Callback<TransactionResponse>> responseCallbackCaptor;
    @Captor
    private ArgumentCaptor<BBMMoneyRequestModel> bbmMoneyRequestModelArgumentCaptor;

    @Test
    public void testPaymentUsingBBMMoney_whenTOkenNull() throws Exception {
        BBMMoneyRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BBMMoneyRequestModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBBMMoney(merchantRestAPIMock, requestModel, null);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }



    @Test
    public void testPaymentUsingBBMMoneySuccess_whenResponseNotNull() throws Exception {
        BBMMoneyRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BBMMoneyRequestModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBBMMoney(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingBBMMoney(xauthCaptor.capture(), bbmMoneyRequestModelArgumentCaptor.capture(), responseCallbackCaptor.capture());

        //response code 200 /201
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionSuccessEvent();

    }


    @Test
    public void testPaymentUsingBBMMoneySuccess_whenResponseNotNull_codenot200() throws Exception {
        BBMMoneyRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BBMMoneyRequestModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBBMMoney(merchantRestAPIMock, requestModel, mToken);

        //response not code 200 /201
        transactionResponse.setStatusCode("300");
        Mockito.verify(merchantRestAPIMock).paymentUsingBBMMoney(xauthCaptor.capture(), bbmMoneyRequestModelArgumentCaptor.capture(), responseCallbackCaptor.capture());
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionFailedEvent();
    }

    @Test
    public void testPaymentUsingBBMMoneySuccess_whenResponseNull() throws Exception {
        BBMMoneyRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BBMMoneyRequestModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBBMMoney(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingBBMMoney(xauthCaptor.capture(), bbmMoneyRequestModelArgumentCaptor.capture(), responseCallbackCaptor.capture());

        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }

    @Test
    public void testPaymentUsingBBMMOneyError_whenValidSSL() throws Exception {
        BBMMoneyRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BBMMoneyRequestModel.class, "sample_pay_card.json");

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBBMMoney(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingBBMMoney(xauthCaptor.capture(), bbmMoneyRequestModelArgumentCaptor.capture(), responseCallbackCaptor.capture());

        //when valid certification
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }

    @Test
    public void testPaymentUsingBBMMOneyError_whenInValidSSL() throws Exception {
        BBMMoneyRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BBMMoneyRequestModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBBMMoney(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingBBMMoney(xauthCaptor.capture(), bbmMoneyRequestModelArgumentCaptor.capture(), responseCallbackCaptor.capture());
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);

        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        // when invalid certification
        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();

    }

    /*
     * using bca klikpay
     */

    @Captor
    private ArgumentCaptor<BCAKlikPayModel> bcaClickModelCaptor;

    @Test
    public void testPaymentUsingBCAKlikPay_whenTokenNull() throws Exception {
        BCAKlikPayModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BCAKlikPayModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBCAClickPay(merchantRestAPIMock, null, requestModel, null);

        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }



    @Test
    public void testPaymentUsingBCAKlikPay_whenResponseNotNull() throws Exception {
        BCAKlikPayModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BCAKlikPayModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBCAClickPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingBCAKlikPay(xauthCaptor.capture(), bcaClickModelCaptor.capture(), responseCallbackCaptor.capture());

        //response code 200 /201
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionSuccessEvent();

    }

    @Test
    public void testPaymentUsingBCAKlikPay_whenResponseNotNull_codenot200() throws Exception {
        BCAKlikPayModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BCAKlikPayModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBCAClickPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);


        //response not code 200 /201
        transactionResponse.setStatusCode("300");
        Mockito.verify(merchantRestAPIMock).paymentUsingBCAKlikPay(xauthCaptor.capture(), bcaClickModelCaptor.capture(), responseCallbackCaptor.capture());
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionFailedEvent();
    }


    @Test
    public void testPaymentUsingBCAKlikPay_whenResponseNull() throws Exception {
        BCAKlikPayModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BCAKlikPayModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse =  null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBCAClickPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingBCAKlikPay(xauthCaptor.capture(), bcaClickModelCaptor.capture(), responseCallbackCaptor.capture());

        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }

    @Test
    public void testPaymentUsingBCAClickPayError_whenValidSSL() throws Exception {
        BCAKlikPayModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BCAKlikPayModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse =  null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBCAClickPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingBCAKlikPay(xauthCaptor.capture(), bcaClickModelCaptor.capture(), responseCallbackCaptor.capture());

        //when valid certification
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }

    @Test
    public void testPaymentUsingBCAClickPayError_invalidSSL() throws Exception {
        BCAKlikPayModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BCAKlikPayModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse =  null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBCAClickPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingBCAKlikPay(xauthCaptor.capture(), bcaClickModelCaptor.capture(), responseCallbackCaptor.capture());
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);

        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();

    }

    /*
     * bca transfer
     */

    @Captor
    private ArgumentCaptor<BCABankTransfer> bcaTranferCaptor;


    @Test
    public void testPaymentUsingBCABank_whenTokenNull() throws Exception {
        BCABankTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BCABankTransfer.class, "sample_pay_bank_bca_transfer.json");

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingPermataBCA(merchantRestAPIMock, transfer, null);

        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }

    @Test
    public void testPaymentUsingBCABankSuccess_whenResponseNotNull() throws Exception {
        BCABankTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BCABankTransfer.class, "sample_pay_bank_bca_transfer.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_bank_bca_transfer.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingPermataBCA(merchantRestAPIMock, transfer, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingBCAVA(xauthCaptor.capture(), bcaTranferCaptor.capture(), responseCallbackCaptor.capture());

        //response code 200 /201
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionSuccessEvent();


    }


    @Test
    public void testPaymentUsingBCABankSuccess_whenResponseNotNull_codenot200() throws Exception {
        BCABankTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BCABankTransfer.class, "sample_pay_bank_bca_transfer.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_bank_bca_transfer.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingPermataBCA(merchantRestAPIMock, transfer, mToken);


        //response not code 200 /201
        transactionResponse.setStatusCode("300");
        Mockito.verify(merchantRestAPIMock).paymentUsingBCAVA(xauthCaptor.capture(), bcaTranferCaptor.capture(), responseCallbackCaptor.capture());
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionFailedEvent();

    }


    @Test
    public void testPaymentUsingBCABankSuccess_whenResponseNull() throws Exception {
        BCABankTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BCABankTransfer.class, "sample_pay_bank_bca_transfer.json");

        TransactionResponse transactionResponse =  null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingPermataBCA(merchantRestAPIMock, transfer, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingBCAVA(xauthCaptor.capture(), bcaTranferCaptor.capture(), responseCallbackCaptor.capture());

        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }

    @Test
    public void testPaymentUsingBCABankError_validSSL() throws Exception {
        BCABankTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BCABankTransfer.class, "sample_pay_bank_bca_transfer.json");

        TransactionResponse transactionResponse =  null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingPermataBCA(merchantRestAPIMock, transfer, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingBCAVA(xauthCaptor.capture(), bcaTranferCaptor.capture(), responseCallbackCaptor.capture());

        //when valid certification
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }

    @Test
    public void testPaymentUsingBCABankError_whenInvalidSSL() throws Exception {
        BCABankTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), BCABankTransfer.class, "sample_pay_bank_bca_transfer.json");

        TransactionResponse transactionResponse =  null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingPermataBCA(merchantRestAPIMock, transfer, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingBCAVA(xauthCaptor.capture(), bcaTranferCaptor.capture(), responseCallbackCaptor.capture());
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);

        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();

    }

    /*
     * using card
     */

    @Captor
    private ArgumentCaptor<CardTransfer> cardTransferCaptor;



    @Test
    public void testPaymentUsingCardError_whenTokenNull() throws Exception {
        CardTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), CardTransfer.class, "sample_pay_card.json");

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingCard(merchantRestAPIMock, null, transfer, null);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }


    @Test
    public void testPaymentUsingCard_whenResponseNotNull() throws Exception {
        CardTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), CardTransfer.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingCard(merchantRestAPIMock, X_AUTH, transfer, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingCard(xauthCaptor.capture(), cardTransferCaptor.capture(), responseCallbackCaptor.capture());

        //response code 200 /201
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionSuccessEvent();


        transactionResponse.setStatusCode("201");
        eventBustImplementSample.paymentUsingCard(merchantRestAPIMock, X_AUTH, transfer, mToken);

        Mockito.verify(merchantRestAPIMock,Mockito.times(2)).paymentUsingCard(xauthCaptor.capture(), cardTransferCaptor.capture(), responseCallbackCaptor.capture());

        //response code 200 /201
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(2)).onTransactionSuccessEvent();


    }

    @Test
    public void testPaymentUsingCard_whenResponseNotNull_not200() throws Exception {
        CardTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), CardTransfer.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingCard(merchantRestAPIMock, X_AUTH, transfer, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingCard(xauthCaptor.capture(), cardTransferCaptor.capture(), responseCallbackCaptor.capture());


        //response not code 200 /201
        transactionResponse.setStatusCode("300");
        Mockito.verify(merchantRestAPIMock).paymentUsingCard(xauthCaptor.capture(), cardTransferCaptor.capture(), responseCallbackCaptor.capture());
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionFailedEvent();
    }

    @Test
    public void testPaymentUsingCard_whenResponseNull() throws Exception {
        CardTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), CardTransfer.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingCard(merchantRestAPIMock, X_AUTH, transfer, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingCard(xauthCaptor.capture(), cardTransferCaptor.capture(), responseCallbackCaptor.capture());

        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }

    @Test
    public void testPaymentUsingCardSSL_whenValidSSL() throws Exception {
        CardTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), CardTransfer.class, "sample_pay_card.json");
        TransactionResponse response = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingCard(merchantRestAPIMock, X_AUTH, transfer, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingCard(xauthCaptor.capture(), cardTransferCaptor.capture(), responseCallbackCaptor.capture());

        //when valid certification
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }


    @Test
    public void testPaymentUsingCard_whenInValidSSL() throws Exception {
        CardTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), CardTransfer.class, "sample_pay_card.json");

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingCard(merchantRestAPIMock, X_AUTH, transfer, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingCard(xauthCaptor.capture(), cardTransferCaptor.capture(), responseCallbackCaptor.capture());
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);

        responseCallbackCaptor.getValue().failure(retrofitErrorMock);

        // when invalid certification
        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();
    }

    @Test
    public void testPaymentUsingCard_whenInValidSSLCertPath() throws Exception {
        CardTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), CardTransfer.class, "sample_pay_card.json");

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingCard(merchantRestAPIMock, X_AUTH, transfer, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingCard(xauthCaptor.capture(), cardTransferCaptor.capture(), responseCallbackCaptor.capture());
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mCertPathValidatorException);

        responseCallbackCaptor.getValue().failure(retrofitErrorMock);

        // when invalid certification
        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();
    }
    /*
     * cimb pay
     */

    @Captor
    private ArgumentCaptor<CIMBClickPayModel> cimbModelCaotor;

    @Test
    public void testPaymentUsingCIMBPay_whenTokenNull() throws Exception {
        CIMBClickPayModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                CIMBClickPayModel.class, "sample_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingCIMBPay(merchantRestAPIMock, null, requestModel, null);

        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }

    @Test
    public void testPaymentUsingCIMBPay_whenResponseNotNull() throws Exception {
        CIMBClickPayModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                CIMBClickPayModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingCIMBPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingCIMBClickPay(xauthCaptor.capture(), cimbModelCaotor.capture(), responseCallbackCaptor.capture());

        //response code 200 /201
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionSuccessEvent();

    }

    @Test
    public void testPaymentUsingCIMBPay_whenResponseNotNull_not200() throws Exception {
        CIMBClickPayModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                CIMBClickPayModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingCIMBPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingCIMBClickPay(xauthCaptor.capture(), cimbModelCaotor.capture(), responseCallbackCaptor.capture());


        //response not code 200 /201
        transactionResponse.setStatusCode("300");
        Mockito.verify(merchantRestAPIMock).paymentUsingCIMBClickPay(xauthCaptor.capture(), cimbModelCaotor.capture(), responseCallbackCaptor.capture());
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionFailedEvent();
    }


    @Test
    public void testPaymentUsingCIMBPay_whenResponseNull() throws Exception {
        CIMBClickPayModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                CIMBClickPayModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingCIMBPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingCIMBClickPay(xauthCaptor.capture(), cimbModelCaotor.capture(), responseCallbackCaptor.capture());

        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }


    @Test
    public void testPaymentUsingCIMBPayError() throws Exception {
        CIMBClickPayModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                CIMBClickPayModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingCIMBPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingCIMBClickPay(xauthCaptor.capture(), cimbModelCaotor.capture(), responseCallbackCaptor.capture());

        //when valid certification
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }

    @Test
    public void testPaymentUsingCIMBPayError_whenInvalidSSL() throws Exception {
        CIMBClickPayModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                CIMBClickPayModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingCIMBPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);

        Mockito.verify(merchantRestAPIMock).paymentUsingCIMBClickPay(xauthCaptor.capture(), cimbModelCaotor.capture(), responseCallbackCaptor.capture());
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);

        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();

    }

    /*
     *
     * epay bri
     */

    @Captor
    private ArgumentCaptor<EpayBriTransfer> epayBRIModelCaptor;

    @Test
    public void testPaymentUsingBRIEpayError_whenTokenNull() throws Exception {
        EpayBriTransfer requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), EpayBriTransfer.class, "sample_pay_card.json");

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBriEpay(merchantRestAPIMock, null, requestModel, null);

        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }

    @Test
    public void testPaymentUsingBRIEpaySuccess_whenResponseNotNull() throws Exception {
        EpayBriTransfer requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), EpayBriTransfer.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBriEpay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingEpayBri(xauthCaptor.capture(), epayBRIModelCaptor.capture(), responseCallbackCaptor.capture());

        //response code 200 /201
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionSuccessEvent();

    }

    @Test
    public void testPaymentUsingBRIEpaySuccess_whenResponseNotNull_not200() throws Exception {
        EpayBriTransfer requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), EpayBriTransfer.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBriEpay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingEpayBri(xauthCaptor.capture(), epayBRIModelCaptor.capture(), responseCallbackCaptor.capture());



        //response not code 200 /201
        transactionResponse.setStatusCode("300");
        Mockito.verify(merchantRestAPIMock).paymentUsingEpayBri(xauthCaptor.capture(), epayBRIModelCaptor.capture(), responseCallbackCaptor.capture());
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionFailedEvent();
    }

    @Test
    public void testPaymentUsingBRIEpaySuccess_whenResponseNull() throws Exception {
        EpayBriTransfer requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), EpayBriTransfer.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBriEpay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingEpayBri(xauthCaptor.capture(), epayBRIModelCaptor.capture(), responseCallbackCaptor.capture());

        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }

    @Test
    public void testPaymentUsingBRIEpayError_whenValidSSL() throws Exception {
        EpayBriTransfer requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), EpayBriTransfer.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBriEpay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingEpayBri(xauthCaptor.capture(), epayBRIModelCaptor.capture(), responseCallbackCaptor.capture());

        //when valid certification
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }
    @Test
    public void testPaymentUsingBRIEpayError_whenInValidSSL() throws Exception {
        EpayBriTransfer requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), EpayBriTransfer.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingBriEpay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingEpayBri(xauthCaptor.capture(), epayBRIModelCaptor.capture(), responseCallbackCaptor.capture());
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);

        //when valid certification
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();

    }

    /*
     *  using indomaret
     */

    @Captor
    private ArgumentCaptor<IndomaretRequestModel> indomaretRequestModelArgumentCaptor;

    @Test
    public void testPaymentUsingIndomaretError_whenTokenNull() throws Exception {
        IndomaretRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), IndomaretRequestModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingIndomaret(merchantRestAPIMock, requestModel, null);

        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }

    @Test
    public void testPaymentUsingIndomaretSuccess_whenResponseNotNull() throws Exception {
        IndomaretRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), IndomaretRequestModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingIndomaret(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingIndomaret(xauthCaptor.capture(), indomaretRequestModelArgumentCaptor.capture(), responseCallbackCaptor.capture());

        //response code 200 /201
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionSuccessEvent();
    }

    @Test
    public void testPaymentUsingIndomaretSuccess_whenResponseNotNull_not200() throws Exception {
        IndomaretRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), IndomaretRequestModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingIndomaret(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingIndomaret(xauthCaptor.capture(), indomaretRequestModelArgumentCaptor.capture(), responseCallbackCaptor.capture());


        //response not code 200 /201
        transactionResponse.setStatusCode("300");
        Mockito.verify(merchantRestAPIMock).paymentUsingIndomaret(xauthCaptor.capture(), indomaretRequestModelArgumentCaptor.capture(), responseCallbackCaptor.capture());
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionFailedEvent();
    }

    @Test
    public void testPaymentUsingIndosatDompetkuSuccess_whenResponseNull() throws Exception {
        IndomaretRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), IndomaretRequestModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingIndomaret(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingIndomaret(xauthCaptor.capture(), indomaretRequestModelArgumentCaptor.capture(), responseCallbackCaptor.capture());

        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }
    @Test
    public void testPaymentUsingIndosatDompetkuError_whenValidSSL() throws Exception {
        IndomaretRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), IndomaretRequestModel.class, "sample_pay_card.json");

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingIndomaret(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingIndomaret(xauthCaptor.capture(), indomaretRequestModelArgumentCaptor.capture(), responseCallbackCaptor.capture());

        //when valid certification
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

        // when invalid certification
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
        Assert.assertNotNull(mSslHandshakeException);
    }

    @Test
    public void testPaymentUsingIndosatDompetkuError_whenInValidSSL() throws Exception {
        IndomaretRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), IndomaretRequestModel.class, "sample_pay_card.json");

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingIndomaret(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingIndomaret(xauthCaptor.capture(), indomaretRequestModelArgumentCaptor.capture(), responseCallbackCaptor.capture());
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);

        //when valid certification
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();

    }

    /*
     * using indosat dompetku
     */
    @Captor
    private ArgumentCaptor<IndosatDompetkuRequest> indosatDompetkuRequestArgumentCaptor;

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
    public void testPaymentUsingIndosatError_tokenNull() throws Exception {
        IndosatDompetkuRequest requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), IndosatDompetkuRequest.class, "sample_pay_card.json");

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingIndosatDompetku(merchantRestAPIMock, requestModel, null);

        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }


    @Test
    public void testPaymentUsingIndosatDompetkuSuccess_whenResponseNotNull() throws Exception {
        IndosatDompetkuRequest requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), IndosatDompetkuRequest.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingIndosatDompetku(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingIndosatDompetku(xauthCaptor.capture(), indosatDompetkuRequestArgumentCaptor.capture(), responseCallbackCaptor.capture());

        //response code 200 /201
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionSuccessEvent();

    }

    @Test
    public void testPaymentUsingIndosatDompetkuError_whenResponseNotNull_codeNot200() throws Exception {
        IndosatDompetkuRequest requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), IndosatDompetkuRequest.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingIndosatDompetku(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingIndosatDompetku(xauthCaptor.capture(), indosatDompetkuRequestArgumentCaptor.capture(), responseCallbackCaptor.capture());

        //response not code 200 /201
        transactionResponse.setStatusCode("300");
        Mockito.verify(merchantRestAPIMock).paymentUsingIndosatDompetku(xauthCaptor.capture(), indosatDompetkuRequestArgumentCaptor.capture(), responseCallbackCaptor.capture());
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionFailedEvent();
    }

    @Test
    public void testPaymentUsingIndosatDompetkusSuccess_whenResponseNull() throws Exception {
        IndosatDompetkuRequest requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), IndosatDompetkuRequest.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingIndosatDompetku(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingIndosatDompetku(xauthCaptor.capture(), indosatDompetkuRequestArgumentCaptor.capture(), responseCallbackCaptor.capture());

        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }

    @Test
    public void testPaymentUsingIndosatDompetkuError_whenvalidSSL() throws Exception {
        IndosatDompetkuRequest requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), IndosatDompetkuRequest.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingIndosatDompetku(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingIndosatDompetku(xauthCaptor.capture(), indosatDompetkuRequestArgumentCaptor.capture(), responseCallbackCaptor.capture());

        //when valid certification
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

        // when invalid certification
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
        Assert.assertNotNull(mSslHandshakeException);
    }

    @Test
    public void testPaymentUsingIndosatDompetkuError_invalidSSL() throws Exception {
        IndosatDompetkuRequest requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), IndosatDompetkuRequest.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingIndosatDompetku(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingIndosatDompetku(xauthCaptor.capture(), indosatDompetkuRequestArgumentCaptor.capture(), responseCallbackCaptor.capture());
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();

    }


    /*
     * using klik BCA
     */


    @Captor
    private ArgumentCaptor<KlikBCAModel> klikBCAModelArgumentCaptor;


    @Test
    public void testPaymentUsingKlikBCASuccess_whenResponseNotNull() throws Exception {
        KlikBCAModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), KlikBCAModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingClickBCAModel(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingKlikBCA( klikBCAModelArgumentCaptor.capture(), responseCallbackCaptor.capture());

        //response code 200 /201
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionSuccessEvent();

        //response not code 200 /201
        transactionResponse.setStatusCode("300");
        Mockito.verify(merchantRestAPIMock).paymentUsingKlikBCA(klikBCAModelArgumentCaptor.capture(), responseCallbackCaptor.capture());
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionFailedEvent();
    }

    @Test
    public void testPaymentUsingKlikBCASuccess_whenResponseNotNull_codeNot200() throws Exception {
        KlikBCAModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), KlikBCAModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingClickBCAModel(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingKlikBCA( klikBCAModelArgumentCaptor.capture(), responseCallbackCaptor.capture());


        transactionResponse.setStatusCode("300");
        Mockito.verify(merchantRestAPIMock).paymentUsingKlikBCA(klikBCAModelArgumentCaptor.capture(), responseCallbackCaptor.capture());
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionFailedEvent();
    }


    @Test
    public void testPaymentUsingKlikBCASuccess_whenResponseNull() throws Exception {
        KlikBCAModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), KlikBCAModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingClickBCAModel(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingKlikBCA( klikBCAModelArgumentCaptor.capture(), responseCallbackCaptor.capture());

        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }


    @Test
    public void testPaymentUsingKlikBCAError_whenvalidSSL() throws Exception {
        KlikBCAModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), KlikBCAModel.class, "sample_pay_card.json");

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingClickBCAModel(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingKlikBCA( klikBCAModelArgumentCaptor.capture(), responseCallbackCaptor.capture());

        //when valid certification
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }

    @Test
    public void testPaymentUsingKlikBCAError_wheInvalidSSL() throws Exception {
        KlikBCAModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), KlikBCAModel.class, "sample_pay_card.json");

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingClickBCAModel(merchantRestAPIMock, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingKlikBCA( klikBCAModelArgumentCaptor.capture(), responseCallbackCaptor.capture());
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);

        //when valid certification
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();

    }

    /*
     * using mandiri bill pay
     */

    @Captor
    private ArgumentCaptor<MandiriBillPayTransferModel> mandiriBillModelCaptor;

    @Test
    public void testPaymentUsingMandiriBillPay_whenTokenNull() throws Exception {
        MandiriBillPayTransferModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                MandiriBillPayTransferModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriBillpay(merchantRestAPIMock, null, requestModel, mToken);

        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }

    @Test
    public void testPaymentUsingMandiriBillPay_whenResponseNotNull() throws Exception {
        MandiriBillPayTransferModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                MandiriBillPayTransferModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriBillpay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriBillPay(xauthCaptor.capture(), mandiriBillModelCaptor.capture(), responseCallbackCaptor.capture());

        //response code 200 /201
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionSuccessEvent();

    }


    @Test
    public void testPaymentUsingMandiriBillPay_whenResponseNotNull_codeNot200() throws Exception {
        MandiriBillPayTransferModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                MandiriBillPayTransferModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriBillpay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriBillPay(xauthCaptor.capture(), mandiriBillModelCaptor.capture(), responseCallbackCaptor.capture());


        //response not code 200 /201
        transactionResponse.setStatusCode("300");
        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriBillPay(xauthCaptor.capture(), mandiriBillModelCaptor.capture(), responseCallbackCaptor.capture());
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionFailedEvent();
    }

    @Test
    public void testPaymentUsingMandiriBillPay_whenResponseNull() throws Exception {
        MandiriBillPayTransferModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                MandiriBillPayTransferModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriBillpay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriBillPay(xauthCaptor.capture(), mandiriBillModelCaptor.capture(), responseCallbackCaptor.capture());

        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }

    @Test
    public void testPaymentUsingMandiriBillPayError_invalid() throws Exception {
        MandiriBillPayTransferModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                MandiriBillPayTransferModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriBillpay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriBillPay(xauthCaptor.capture(), mandiriBillModelCaptor.capture(), responseCallbackCaptor.capture());

        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();



    }

    @Test
    public void testPaymentUsingMandiriBillPayError() throws Exception {
        MandiriBillPayTransferModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                MandiriBillPayTransferModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriBillpay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriBillPay(xauthCaptor.capture(), mandiriBillModelCaptor.capture(), responseCallbackCaptor.capture());

        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Assert.assertNotNull(mSslHandshakeException);
    }

    /*
     * using mandiri click pay
     */

    @Captor
    private ArgumentCaptor<MandiriClickPayRequestModel> mandiriClickPayCaptor;

    @Test
    public void testPaymentUsingMandiriClickPayError_whenTokenNull() throws Exception {
        MandiriClickPayRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), MandiriClickPayRequestModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriClickPay(merchantRestAPIMock, null, requestModel, mToken);

        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();


    }

    @Test
    public void testPaymentUsingMandiriClickPay_whenResponseNotNull() throws Exception {
        MandiriClickPayRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), MandiriClickPayRequestModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriClickPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriClickPay(xauthCaptor.capture(), mandiriClickPayCaptor.capture(), responseCallbackCaptor.capture());

        //response code 200 /201
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionSuccessEvent();


    }

    @Test
    public void testPaymentUsingMandiriClickPay_whenResponseNotNull_responseNot200() throws Exception {
        MandiriClickPayRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), MandiriClickPayRequestModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriClickPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriClickPay(xauthCaptor.capture(), mandiriClickPayCaptor.capture(), responseCallbackCaptor.capture());


        //response not code 200 /201
        transactionResponse.setStatusCode("300");
        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriClickPay(xauthCaptor.capture(), mandiriClickPayCaptor.capture(), responseCallbackCaptor.capture());
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionFailedEvent();
    }


    @Test
    public void testPaymentUsingMandiriClickPay_whenResponseNull() throws Exception {
        MandiriClickPayRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), MandiriClickPayRequestModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriClickPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriClickPay(xauthCaptor.capture(), mandiriClickPayCaptor.capture(), responseCallbackCaptor.capture());

        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }

    @Test
    public void testPaymentUsingMandiriClickPayError_validCertificate() throws Exception {
        MandiriClickPayRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), MandiriClickPayRequestModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriClickPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriClickPay(xauthCaptor.capture(), mandiriClickPayCaptor.capture(), responseCallbackCaptor.capture());


        //when invalid certification
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();


    }

    @Test
    public void testPaymentUsingMandiriClickPayError_whenInvalidCertificated() throws Exception {
        MandiriClickPayRequestModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), MandiriClickPayRequestModel.class, "sample_pay_card.json");

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriClickPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriClickPay(xauthCaptor.capture(), mandiriClickPayCaptor.capture(), responseCallbackCaptor.capture());

        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
        // when valid certification
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();


    }

    /*
     * mandiri ecash
     */

    @Captor
    private ArgumentCaptor<MandiriECashModel> mandiriEcashModelCaptor;

    @Test
    public void testPaymentUsingMandiriEcashPay_whenTokenNull() throws Exception {
        MandiriECashModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), MandiriECashModel.class, "sample_pay_card.json");

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriEcashPay(merchantRestAPIMock, null, requestModel, null);

        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }

    @Test
    public void testPaymentUsingMandiriEcashPay_whenResponseNotNull() throws Exception {
        MandiriECashModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), MandiriECashModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriEcashPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriECash(xauthCaptor.capture(), mandiriEcashModelCaptor.capture(), responseCallbackCaptor.capture());

        //response code 200 /201
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionSuccessEvent();

        //response not code 200 /201
        transactionResponse.setStatusCode("300");
        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriECash(xauthCaptor.capture(), mandiriEcashModelCaptor.capture(), responseCallbackCaptor.capture());
        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionFailedEvent();
    }


    @Test
    public void testPaymentUsingMandiriEcashPay_whenResponseNull() throws Exception {
        MandiriECashModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), MandiriECashModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse =  null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriEcashPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriECash(xauthCaptor.capture(), mandiriEcashModelCaptor.capture(), responseCallbackCaptor.capture());

        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }

    @Test
    public void testPaymentUsingMandiriEcashPayError() throws Exception {
        MandiriECashModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), MandiriECashModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse =  null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriEcashPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriECash(xauthCaptor.capture(), mandiriEcashModelCaptor.capture(), responseCallbackCaptor.capture());

        //when valid certification
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }


    @Test
    public void testPaymentUsingMandiriEcash_validSSL() throws Exception {
        MandiriECashModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), MandiriECashModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse =  null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriEcashPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriECash(xauthCaptor.capture(), mandiriEcashModelCaptor.capture(), responseCallbackCaptor.capture());

        //when valid certification
        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

    }

    @Test
    public void testPaymentUsingMandiriEcash_invalidSSL() throws Exception {
        MandiriECashModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), MandiriECashModel.class, "sample_pay_card.json");

        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriEcashPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingMandiriECash(xauthCaptor.capture(), mandiriEcashModelCaptor.capture(), responseCallbackCaptor.capture());
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);

        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();

        //certpath error

        eventBustImplementSample.paymentUsingMandiriEcashPay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        Mockito.verify(merchantRestAPIMock, Mockito.times(2)).paymentUsingMandiriECash(xauthCaptor.capture(), mandiriEcashModelCaptor.capture(), responseCallbackCaptor.capture());
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mCertPathValidatorException);

        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(2)).onSSLErrorEvent();
    }

    /*
     * permata bank
     */

    @Captor
    private ArgumentCaptor<Callback<TransactionResponse>> transferResponCaptor;

    @Captor
    private ArgumentCaptor<PermataBankTransfer> permataTranferCaptor;

    @Test
    public void testPaymentUsingPermataBankSuccess_whenTokenNull() throws Exception {
        PermataBankTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), PermataBankTransfer.class, "sample_permata_bank_transfer.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_permata_bank_transfer.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingPermataBank(merchantRestAPIMock, transfer, null);

        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }

    @Test
    public void testPaymentUsingPermataBankSuccess_whenResponseNotNull() throws Exception {
        PermataBankTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), PermataBankTransfer.class, "sample_permata_bank_transfer.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_permata_bank_transfer.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingPermataBank(merchantRestAPIMock, transfer, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingPermataBank(xauthCaptor.capture(), permataTranferCaptor.capture(), transferResponCaptor.capture());

        //response code 200 /201
        transferResponCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionSuccessEvent();


    }

    @Test
    public void testPaymentUsingPermataBankSuccess_whenResponseNotNull201() throws Exception {
        PermataBankTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), PermataBankTransfer.class, "sample_permata_bank_transfer.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_permata_bank_transfer.json");
        transactionResponse.setStatusCode("200");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingPermataBank(merchantRestAPIMock, transfer, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingPermataBank(xauthCaptor.capture(), permataTranferCaptor.capture(), transferResponCaptor.capture());

        //response code 200 /201
        transferResponCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionSuccessEvent();


    }

    @Test
    public void testPaymentUsingPermataBankSuccess_whenResponseNotNullNot200() throws Exception {
        PermataBankTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), PermataBankTransfer.class, "sample_permata_bank_transfer.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_permata_bank_transfer.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingPermataBank(merchantRestAPIMock, transfer, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingPermataBank(xauthCaptor.capture(), permataTranferCaptor.capture(), transferResponCaptor.capture());

        //response not code 200 /201
        transactionResponse.setStatusCode("300");
        Mockito.verify(merchantRestAPIMock).paymentUsingPermataBank(xauthCaptor.capture(), permataTranferCaptor.capture(), transferResponCaptor.capture());
        transferResponCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onTransactionFailedEvent();

    }

    @Test
    public void testPaymentUsingPermataBankSuccess_whenResponseNull() throws Exception {
        PermataBankTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), PermataBankTransfer.class, "sample_permata_bank_transfer.json");

        TransactionResponse transactionResponse =  null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingPermataBank(merchantRestAPIMock, transfer, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingPermataBank(xauthCaptor.capture(), permataTranferCaptor.capture(), transferResponCaptor.capture());

        //response code 200 /201
        transferResponCaptor.getValue().success(transactionResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }


    @Test
    public void testPaymentUsingPermataBankError_validSSL() throws Exception {
        PermataBankTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), PermataBankTransfer.class, "sample_permata_bank_transfer.json");

        TransactionResponse transactionResponse =  null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingPermataBank(merchantRestAPIMock, transfer, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingPermataBank(xauthCaptor.capture(), permataTranferCaptor.capture(), transferResponCaptor.capture());

        transferResponCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();
    }

    @Test
    public void testPaymentUsingPermataBankError_invalidSSL() throws Exception {
        PermataBankTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), PermataBankTransfer.class, "sample_permata_bank_transfer.json");

        TransactionResponse transactionResponse =  null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingPermataBank(merchantRestAPIMock, transfer, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingPermataBank(xauthCaptor.capture(), permataTranferCaptor.capture(), responseCallbackCaptor.capture());
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);

        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();
    }

    @Test
    public void testPaymentUsingPermataBankError_invalidSSLCertPath() throws Exception {
        PermataBankTransfer transfer = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(), PermataBankTransfer.class, "sample_permata_bank_transfer.json");

        TransactionResponse transactionResponse =  null;
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingPermataBank(merchantRestAPIMock, transfer, mToken);

        Mockito.verify(merchantRestAPIMock).paymentUsingPermataBank(xauthCaptor.capture(), permataTranferCaptor.capture(), responseCallbackCaptor.capture());
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mCertPathValidatorException);

        responseCallbackCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onSSLErrorEvent();
    }
}
