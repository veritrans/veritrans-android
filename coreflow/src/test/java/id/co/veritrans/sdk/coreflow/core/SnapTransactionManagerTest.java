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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import id.co.veritrans.sdk.coreflow.R;
import id.co.veritrans.sdk.coreflow.SDKConfigTest;
import id.co.veritrans.sdk.coreflow.analytics.MixpanelApi;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBus;
import id.co.veritrans.sdk.coreflow.models.SnapTokenDetailResponse;
import id.co.veritrans.sdk.coreflow.transactionmanager.BusCollaborator;
import id.co.veritrans.sdk.coreflow.transactionmanager.EventBustImplementSample;
import retrofit.Callback;
import retrofit.client.Response;

/**
 * Created by ziahaqi on 7/18/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, TextUtils.class, Logger.class, Looper.class, Base64.class})
@PowerMockIgnore("javax.net.ssl.*")
public class SnapTransactionManagerTest {
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
    protected BusCollaborator busCollaborator;

    @InjectMocks
    protected EventBustImplementSample eventBusImplementSample;
    @Mock
    private VeritransBus veritransBusMock;
    @Mock
    private VeritransRestAPI vertitransApi;
    @Mock
    private ArgumentCaptor<Callback<SnapTokenDetailResponse>> callbackSnapTokenResponseCaptor;

    @Mock
    private SnapTokenDetailResponse snapTokenDetailResponse;
    private String tokenId = "tokenId";
    @Mock
    private MerchantRestAPI merchantApi;
    private Response retrofitResponse;

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

    }

    @Test
    public void getTokenTestSuccess(){
        Mockito.when(snapTokenDetailResponse.getTokenid()).thenReturn(tokenId);
        snapTokenDetailResponse = new SnapTokenDetailResponse();
        eventBusImplementSample.setTransactionManager(transactionManager);
        eventBusImplementSample.getSnapToken(merchantApi);

        Mockito.verify(vertitransApi, Mockito.times(1)).getSnapToken(callbackSnapTokenResponseCaptor.capture());

        callbackSnapTokenResponseCaptor.getValue().success(snapTokenDetailResponse, retrofitResponse);
        Mockito.verify(busCollaborator, Mockito.times(1)).onGetTokenSuccessEvent();
    }
}
