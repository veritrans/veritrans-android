package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.midtrans.sdk.corekit.SDKConfigTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * Created by ziahaqi on 26/06/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, NetworkInfo.class})
public class SDKCoreFlowBuilderTest {
    @Mock
    Context context;
    @Mock
    Resources resources;
    @Mock
    ConnectivityManager connectivityManager;
    @Mock
    NetworkInfo networkInfo;
    @Mock
    SdkCoreFlowBuilder sdkCoreFlowBuilderMock;
    @InjectMocks
    private SdkCoreFlowBuilder sdkCoreFlowBuilder;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
        Mockito.when(context.getApplicationContext()).thenReturn(context);
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);

        sdkCoreFlowBuilder = SdkCoreFlowBuilder.init(context, SDKConfigTest.CLIENT_KEY, SDKConfigTest.MERCHANT_BASE_URL);
    }

    @Test
    public void isValidDataFailedTest() {
        sdkCoreFlowBuilder = SdkCoreFlowBuilder.init(context, null, SDKConfigTest.MERCHANT_BASE_URL);
        sdkCoreFlowBuilder.isValidData();
        verifyStatic(Mockito.times(1));
        Log.e(Matchers.anyString(), Matchers.anyString(), Matchers.any(Throwable.class));
    }
}
