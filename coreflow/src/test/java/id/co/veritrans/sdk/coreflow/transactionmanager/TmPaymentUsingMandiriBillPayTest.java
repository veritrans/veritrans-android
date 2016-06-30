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
import id.co.veritrans.sdk.coreflow.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.APIClientMain;
import id.co.veritrans.sdk.coreflow.restapi.RestAPIMocUtilites;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by ziahaqi on 28/06/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, TextUtils.class, Logger.class, Looper.class, Base64.class})
public class TmPaymentUsingMandiriBillPayTest extends APIClientMain {
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
    private ArgumentCaptor<Callback<TransactionResponse>> responseCallbackCaptor;
    @Captor
    private ArgumentCaptor<MandiriBillPayTransferModel> mandiriBillModelCaptor;

    @Before
    public void setup(){
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Looper.class);
        PowerMockito.mockStatic(Logger.class);

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
    public void testPaymentUsingMandiriBillPay_whenTokenNull() throws Exception {
        MandiriBillPayTransferModel requestModel = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                MandiriBillPayTransferModel.class, "sample_pay_card.json");

        TransactionResponse transactionResponse = RestAPIMocUtilites.getSampleDataFromFile(this.getClass().getClassLoader(),
                TransactionResponse.class, "sample_response_pay_card.json");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.paymentUsingMandiriBillpay(merchantRestAPIMock, X_AUTH, requestModel, mToken);

        responseCallbackCaptor.getValue().success(transactionResponse, retrofitResponse);
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
}
