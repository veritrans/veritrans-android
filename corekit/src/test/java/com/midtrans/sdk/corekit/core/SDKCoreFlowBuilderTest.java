package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.midtrans.sdk.corekit.SDKConfigTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

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
        Assert.assertTrue(sdkCoreFlowBuilder.isValidData());
        sdkCoreFlowBuilder = SdkCoreFlowBuilder.init(context, null, SDKConfigTest.MERCHANT_BASE_URL);
        Assert.assertFalse(sdkCoreFlowBuilder.isValidData());
    }
}
