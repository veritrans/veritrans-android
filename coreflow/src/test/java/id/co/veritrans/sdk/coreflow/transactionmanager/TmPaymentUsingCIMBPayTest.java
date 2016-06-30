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
import id.co.veritrans.sdk.coreflow.models.CIMBClickPayModel;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.APIClientMain;
import id.co.veritrans.sdk.coreflow.restapi.RestAPIMocUtilites;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by ziahaqi on 28/06/2016.
 */
public class TmPaymentUsingCIMBPayTest extends TransactionMangerMain{
    @Captor
    private ArgumentCaptor<String> xauthCaptor;
    @Captor
    private ArgumentCaptor<Callback<TransactionResponse>> responseCallbackCaptor;
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

        // when invalid certification
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
        Assert.assertNotNull(mSslHandshakeException);
    }

}
