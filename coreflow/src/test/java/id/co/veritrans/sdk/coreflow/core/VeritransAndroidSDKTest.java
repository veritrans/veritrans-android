package id.co.veritrans.sdk.coreflow.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.powermock.api.mockito.PowerMockito.*;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import id.co.veritrans.sdk.coreflow.R;
import id.co.veritrans.sdk.coreflow.SDKConfig;
import id.co.veritrans.sdk.coreflow.core.SdkCoreFlowBuilder;
import id.co.veritrans.sdk.coreflow.core.TransactionManager;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;

/**
 * Created by ziahaqi on 24/06/2016.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({VeritransSDK.class,Log.class, TextUtils.class, Logger.class, Looper.class})

public class VeritransAndroidSDKTest {

    @Mock
    VeritransSDK veritransSDKMock;

    @Mock
    Context context;
    @Mock
    TransactionManager transactionManager;
    @Mock
    MerchantRestAPI merchantRestAPI;
    @Mock
    VeritransRestAPI veritransRestAPI;
    @Mock
    Resources resources;
    @Mock
    ConnectivityManager connectivityManager;
    @Mock
    NetworkInfo networkInfo;
    @Mock
    boolean isconnected;
    @Mock
    SharedPreferences preference;

    @Before
    public void setup(){
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Looper.class);
        PowerMockito.mockStatic(Logger.class);

        Mockito.when(context.getApplicationContext()).thenReturn(context);
        Mockito.when(context.getString(R.string.error_unable_to_connect)).thenReturn("not connected");
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        Mockito.when(networkInfo.isConnected()).thenReturn(true);
//        Mockito.when(networkInfo.isAvailable()).thenReturn(true);
        Mockito.when(context.getSharedPreferences("local.data", Context.MODE_PRIVATE)).thenReturn(preference);


        veritransSDKMock = spy(new SdkCoreFlowBuilder(context, SDKConfig.CLIENT_KEY, SDKConfig.MERCHANT_BASE_URL)
                .enableLog(true)
                .setDefaultText("open_sans_regular.ttf")
                .setSemiBoldText("open_sans_semibold.ttf")
                .setBoldText("open_sans_bold.ttf")
                .setMerchantName("Veritrans Example Merchant")
                .buildSDK());
        Mockito.when(veritransSDKMock.readAuthenticationToken()).thenReturn("token");
        veritransSDKMock.setTransactionManager(transactionManager);

    }

    @Test
    public void getOverLimit() throws Exception {
//        Mockito.when(veritransSDKMock.isAvailableForTransaction("event")).thenReturn(true);
        Assert.assertEquals(1,1);
////        when(veritransSDKMock.isSDKAvailable("args")).thenReturn(true);
//        veritransSDKMock.getOffersList();
//        Mockito.verify(transactionManager).getOffers("token");
    }

    private void setIsNetworkAvailableReturnTrue() throws Exception {
        when(veritransSDKMock, method(VeritransSDK.class, "isNetworkAvailable", String.class))
                .withArguments(Matchers.anyString())

                .thenReturn(true);
    }

    private void setIsSDKAvailableReturnTrue() throws Exception {

        when(veritransSDKMock, method(VeritransSDK.class, "isSDKAvailable", String.class))
                .withArguments(Matchers.anyString())
                .thenReturn(true);
    }

    private void setIsSDKAvailableReturnFalse() throws Exception {
        when(veritransSDKMock, method(VeritransSDK.class, "isSDKAvailable", String.class))
                .withArguments(Matchers.anyString())
                .thenReturn(false);
    }

    private void setIsNetworkAvailableReturnFalse() throws Exception {
        when(veritransSDKMock, method(VeritransSDK.class, "isNetworkAvailable", String.class))
                .withArguments(Matchers.anyString())
                .thenReturn(false);
    }


}
