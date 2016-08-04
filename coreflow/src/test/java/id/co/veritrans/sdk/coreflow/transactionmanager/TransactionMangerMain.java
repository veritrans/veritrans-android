package id.co.veritrans.sdk.coreflow.transactionmanager;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.cert.CertPathValidatorException;

import javax.net.ssl.SSLHandshakeException;

import id.co.veritrans.sdk.coreflow.APIClientMain;
import id.co.veritrans.sdk.coreflow.R;
import id.co.veritrans.sdk.coreflow.analytics.MixpanelApi;
import id.co.veritrans.sdk.coreflow.core.EventBusImplementSample;
import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.core.MerchantRestAPI;
import id.co.veritrans.sdk.coreflow.core.MixpanelAnalyticsManager;
import id.co.veritrans.sdk.coreflow.core.SdkCoreFlowBuilder;
import id.co.veritrans.sdk.coreflow.core.TransactionManager;
import id.co.veritrans.sdk.coreflow.core.VeritransAndroidSDKTest;
import id.co.veritrans.sdk.coreflow.core.VeritransRestAPI;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBus;
import retrofit.RetrofitError;

/**
 * Created by ziahaqi on 30/06/2016.
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, TextUtils.class, Logger.class, Looper.class, Base64.class})
@PowerMockIgnore("javax.net.ssl.*")
public abstract class TransactionMangerMain extends APIClientMain{
    protected TransactionManager transactionManager;
    @Mock
    protected Context context;
    @Mock
    protected Resources resources;
    @Mock
    protected ConnectivityManager connectivityManager;

    @Mock
    protected SSLHandshakeException mSslHandshakeException;
    @Mock
    protected CertPathValidatorException mCertPathValidatorException;

    @Mock
    protected VeritransRestAPI veritransRestAPIMock;
    @Mock
    protected MerchantRestAPI merchantRestAPIMock;

    @Mock
    protected RetrofitError retrofitErrorMock;
    @Mock
    protected MixpanelAnalyticsManager mixpanelAnalyticsManagerMock;
    @Mock
    protected MixpanelApi mixpanelApiMock;

    @Mock
    protected BusCollaborator busCollaborator;

    @InjectMocks
    protected EventBusImplementSample eventBustImplementSample;
    @Mock
    protected VeritransBus veritransBus;

    protected VeritransSDK veritransSDK;
    protected String sampleJsonResponse = "{\"a\":\"a\"}";
    protected String mToken = "VT-423wedwe4324r34";

    @Before
    public void setup(){
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Looper.class);
        PowerMockito.mockStatic(Base64.class);
        PowerMockito.mockStatic(Logger.class);

        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(context.getApplicationContext()).thenReturn(context);
        Mockito.when(context.getString(R.string.success_code_200)).thenReturn("200");
        Mockito.when(context.getString(R.string.success_code_201)).thenReturn("201");
        Mockito.when(context.getString(R.string.success)).thenReturn("success");

        veritransSDK = new SdkCoreFlowBuilder(context, "SDK", "hi")
                .enableLog(true)
                .setDefaultText("open_sans_regular.ttf")
                .setSemiBoldText("open_sans_semibold.ttf")
                .setBoldText("open_sans_bold.ttf")
                .setMerchantName("Veritrans Example Merchant")
                .buildSDK();
        mixpanelAnalyticsManagerMock.setMixpanelApi(mixpanelApiMock);
        transactionManager = veritransSDK.getTransactionManager();
        transactionManager.setAnalyticsManager(mixpanelAnalyticsManagerMock);

    }
}
