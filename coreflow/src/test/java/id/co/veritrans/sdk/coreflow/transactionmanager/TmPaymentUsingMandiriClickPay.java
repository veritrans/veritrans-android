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
import id.co.veritrans.sdk.coreflow.SDKConfig;
import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.core.MerchantRestAPI;
import id.co.veritrans.sdk.coreflow.core.SdkCoreFlowBuilder;
import id.co.veritrans.sdk.coreflow.core.TransactionManager;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBus;
import id.co.veritrans.sdk.coreflow.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.APIClientMain;
import id.co.veritrans.sdk.coreflow.restapi.RestAPIMocUtilites;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by ziahaqi on 28/06/2016.
 */

public class TmPaymentUsingMandiriClickPay extends TransactionMangerMain {
    private String mToken = "VT-423wedwe4324r34";
    @Captor
    private ArgumentCaptor<String> xauthCaptor;
    @Captor
    private ArgumentCaptor<Callback<TransactionResponse>> responseCallbackCaptor;
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

}
