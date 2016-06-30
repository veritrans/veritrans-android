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
import id.co.veritrans.sdk.coreflow.models.AuthModel;
import id.co.veritrans.sdk.coreflow.APIClientMain;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by ziahaqi on 29/06/2016.
 */
public class TmGetAuthenticationTokenTest extends TransactionMangerMain {



    @Captor
    private ArgumentCaptor<Callback<AuthModel>> callbackArgumentCaptor;
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
    private ArgumentCaptor<String> instalmentTermCaptor;
    @Captor
    private ArgumentCaptor<Boolean> scureCaptor;
    @Captor
    private ArgumentCaptor<Boolean> twoClickCaptor;
    @Captor
    private ArgumentCaptor<Double> grossAmountCaptor;

    @Captor
    ArgumentCaptor<VeritransBus> captor = ArgumentCaptor
            .forClass(VeritransBus.class);
    @Captor
    private ArgumentCaptor<String> tokenIdCaptor;

    @Before
    public void setup(){
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Looper.class);
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(Base64.class);
        PowerMockito.mockStatic(Logger.class);
        PowerMockito.mock(ConnectivityManager.class);

        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(context.getApplicationContext()).thenReturn(context);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(context.getString(R.string.success_code_200)).thenReturn("200");

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
    public void testGetAuthenticationTokenSuccess(){
        AuthModel model= new AuthModel();
        model.setxAuth("481111-1114-c4623fb5-5bfe-4b58-83c5-15794a10239e");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getAuthenticationToken(merchantRestAPIMock);

        Mockito.verify(merchantRestAPIMock, Mockito.times(1)).getAuthenticationToken(callbackArgumentCaptor.capture());

        callbackArgumentCaptor.getValue().success(model, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onAuthenticationEvent();

    }

    @Test
    public void testGetAuthenticationTokenError(){
        AuthModel model= new AuthModel();
        model.setxAuth("481111-1114-c4623fb5-5bfe-4b58-83c5-15794a10239e");
        eventBustImplementSample.setTransactionManager(transactionManager);
        eventBustImplementSample.registerBus(veritransBus);
        eventBustImplementSample.getAuthenticationToken(merchantRestAPIMock);

        Mockito.verify(merchantRestAPIMock, Mockito.times(1)).getAuthenticationToken(callbackArgumentCaptor.capture());


        //when valid certification
        callbackArgumentCaptor.getValue().failure(retrofitErrorMock);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGeneralErrorEvent();

        // when invalid certification
        Mockito.when(retrofitErrorMock.getCause()).thenReturn(mSslHandshakeException);
        Assert.assertNotNull(mSslHandshakeException);

    }

}
