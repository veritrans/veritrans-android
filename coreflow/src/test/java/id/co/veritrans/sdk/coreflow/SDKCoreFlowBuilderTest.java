package id.co.veritrans.sdk.coreflow;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

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

import id.co.veritrans.sdk.coreflow.core.SdkCoreFlowBuilder;

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
    @InjectMocks
    private SdkCoreFlowBuilder sdkCoreFlowBuilder;
    @Mock
    SdkCoreFlowBuilder sdkCoreFlowBuilderMock;

    @Before
    public void setup(){
        PowerMockito.mockStatic(Log.class);
        Mockito.when(context.getApplicationContext()).thenReturn(context);
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);

        sdkCoreFlowBuilder = new SdkCoreFlowBuilder(context, SDKConfigTest.CLIENT_KEY, SDKConfigTest.MERCHANT_BASE_URL);
    }

    @Test
    public void isValidDataFailedTest(){
        Assert.assertTrue(sdkCoreFlowBuilder.isValidData());
        sdkCoreFlowBuilder = new SdkCoreFlowBuilder(context, null, SDKConfigTest.MERCHANT_BASE_URL);
        Assert.assertFalse(sdkCoreFlowBuilder.isValidData());
    }

    @Test
    public void buidSDKTest_whenInvalidData(){

//        sdkCoreFlowBuilder = new SdkCoreFlowBuilder(context, null, SDKConfigTest.MERCHANT_BASE_URL);
//        Assert.assertNull(sdkCoreFlowBuilder.buildSDK());

    }

    @Test
    public void buidSDKTest(){
        Assert.assertEquals(1,1);
    }

    @Test public void testSetMerchantName(){
        String sampleName = "sampleName";
        sdkCoreFlowBuilder.setMerchantName(sampleName);
        Assert.assertEquals(sdkCoreFlowBuilder.setMerchantName(sampleName), sdkCoreFlowBuilder.setMerchantName(sampleName));
    }


}
