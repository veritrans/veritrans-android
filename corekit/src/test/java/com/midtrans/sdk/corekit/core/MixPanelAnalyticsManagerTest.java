package com.midtrans.sdk.corekit.core;

import android.util.Base64;
import android.util.Log;

import com.midtrans.sdk.corekit.analytics.MixpanelApi;
import com.midtrans.sdk.corekit.analytics.MixpanelEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by ziahaqi on 7/19/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MidtransSDK.class, Log.class, Logger.class, Base64.class})

public class MixPanelAnalyticsManagerTest {

    protected String sampleJsonResponse = "{\"response\":\"response\"}";
    protected Response retrofitResponse = new Response("URL", 200, "success", Collections.EMPTY_LIST,
            new TypedByteArray("application/sampleJsonResponse", sampleJsonResponse.getBytes()));
    private MixpanelAnalyticsManager analyticsManager;
    @Mock
    private MixpanelApi mixpanelApiMock;
    @Mock
    private MixpanelEvent mixPanelEventMock;
    @Captor
    private ArgumentCaptor<String> datacaptor;
    @Captor
    private ArgumentCaptor<Callback<Integer>> responseCaptor;
    @Mock
    private Integer mixpanelResponseMock;
    @Mock
    private RetrofitError retrofitErrorMock;
    private long time = 10;
    private String eventName = "eventName";
    private String paymentType = "paymentType";
    @Mock
    private MidtransSDK sdkMock;
    private MixpanelAnalyticsManager mixpanelAnalyticsManagerSpy;
    private String bankType = "bankType";
    private String erorMessage = "errorMessage";

    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Base64.class);
        PowerMockito.mockStatic(Logger.class);

        PowerMockito.mockStatic(MidtransSDK.class);
        analyticsManager = new MixpanelAnalyticsManager(mixpanelApiMock);
        mixPanelEventMock = new MixpanelEvent();
        Mockito.when(MidtransSDK.getInstance()).thenReturn(sdkMock);
        mixpanelAnalyticsManagerSpy = Mockito.spy(analyticsManager);

    }

    @Test
    public void setMixPanelTest_success() {
        analyticsManager.setMixpanelApi(mixpanelApiMock);
        analyticsManager.trackEvent(mixPanelEventMock);

        Mockito.verify(mixpanelApiMock).trackEvent(datacaptor.capture(), responseCaptor.capture());
        responseCaptor.getValue().success(mixpanelResponseMock, retrofitResponse);
        PowerMockito.verifyStatic(Mockito.times(1));
        Logger.i(Matchers.anyString());
    }

    @Test
    public void setMixPanelTesterror_whenApiNull() {
        analyticsManager.setMixpanelApi(null);
        analyticsManager.trackEvent(mixPanelEventMock);
        PowerMockito.verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString());
    }

    @Test
    public void setMixPanelTestError_whenFailed() {
        analyticsManager.setMixpanelApi(mixpanelApiMock);
        analyticsManager.trackEvent(mixPanelEventMock);

        Mockito.verify(mixpanelApiMock).trackEvent(datacaptor.capture(), responseCaptor.capture());
        responseCaptor.getValue().failure(retrofitErrorMock);
        PowerMockito.verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString());
    }

    @Test
    public void trackMixPanel1() {
        mixpanelAnalyticsManagerSpy.trackMixpanel(eventName, paymentType, time);
        Mockito.verify(mixpanelAnalyticsManagerSpy).trackEvent(Matchers.any(MixpanelEvent.class));
    }

    @Test
    public void trackMixPane2() {
        mixpanelAnalyticsManagerSpy.trackMixpanel(eventName, paymentType, bankType, time);
        Mockito.verify(mixpanelAnalyticsManagerSpy).trackEvent(Matchers.any(MixpanelEvent.class));
    }

    @Test
    public void trackMixPane3() {
        mixpanelAnalyticsManagerSpy.trackMixpanel(eventName, paymentType, time, erorMessage);
        Mockito.verify(mixpanelAnalyticsManagerSpy).trackEvent(Matchers.any(MixpanelEvent.class));
    }

    @Test
    public void trackMixPane4() {
        mixpanelAnalyticsManagerSpy.trackMixpanel(eventName, paymentType, bankType, time, erorMessage);
        Mockito.verify(mixpanelAnalyticsManagerSpy).trackEvent(Matchers.any(MixpanelEvent.class));
    }
}
