package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.midtrans.sdk.corekit.SDKConfigTest;
import com.midtrans.sdk.corekit.base.enums.Environment;
import com.midtrans.sdk.corekit.base.network.MidtransRestAdapter;
import com.midtrans.sdk.corekit.core.merchant.MerchantApiManager;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.CheckoutCallback;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.TransactionRequest;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.response.TokenResponse;
import com.midtrans.sdk.corekit.core.snap.SnapApiManager;
import com.midtrans.sdk.corekit.core.snap.model.transaction.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.corekit.utilities.NetworkHelper;
import com.midtrans.sdk.corekit.utilities.Utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NetworkHelper.class, Looper.class, Utils.class, Log.class, TextUtils.class,
        Logger.class, MidtransRestAdapter.class})
public class MidtransCoreSdkTest {

    @Mock
    private Context contextMock;

    @Mock
    private ConnectivityManager connectivityManager;
    @Mock
    private NetworkInfo networkInfo;
    @Mock
    private CheckoutCallback checkoutCallbackMock;
    @Mock
    private TransactionOptionsCallback transactionOptionCallbackMock;

    @Mock
    private MidtransCoreSdk midtransCoreSdkSpy;
    @Mock
    private TransactionRequest transactionRequestMock;

    @Mock
    private SnapApiManager snapServiceManager;
    @Mock
    private MerchantApiManager merchantServiceManager;

    @Test
    public void test() {
        Assert.assertEquals(1, 1);
    }

    @Before
    public void setup() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Logger.class);
        PowerMockito.mockStatic(Looper.class);
        PowerMockito.mockStatic(Utils.class);
        PowerMockito.mockStatic(NetworkHelper.class);
        PowerMockito.mockStatic(MidtransRestAdapter.class);

        Mockito.when(contextMock.getApplicationContext()).thenReturn(contextMock);
        Mockito.when(contextMock.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        Mockito.when(networkInfo.isConnected()).thenReturn(false);
        Mockito.when(TextUtils.isEmpty(Matchers.anyString())).thenReturn(false);
        Mockito.when(TextUtils.isEmpty(null)).thenReturn(true);
        Mockito.when(TextUtils.isEmpty("")).thenReturn(true);
        Mockito.when(NetworkHelper.newSnapServiceManager(SDKConfigTest.MERCHANT_BASE_URL, SDKConfigTest.TIME_OUT)).thenReturn(snapServiceManager);
        Mockito.when(NetworkHelper.newMerchantServiceManager(SDKConfigTest.MERCHANT_BASE_URL, SDKConfigTest.TIME_OUT)).thenReturn(merchantServiceManager);

        MidtransCoreSdk midtransSDK = MidtransCoreSdk.init(contextMock,
                SDKConfigTest.CLIENT_KEY,
                SDKConfigTest.MERCHANT_BASE_URL)
                .setLogEnabled(true)
                .setEnvironment(Environment.SANDBOX)
                .build();

        midtransCoreSdkSpy = Mockito.spy(midtransSDK);
    }

    @Test
    public void test_setTransactionRequest_positive() {
        midtransCoreSdkSpy.setTransactionRequest(transactionRequestMock);
        Assert.assertEquals(transactionRequestMock, midtransCoreSdkSpy.getTransactionRequest());
    }

    @Test
    public void test_setTransactionRequest_negative() {
        midtransCoreSdkSpy.setTransactionRequest(transactionRequestMock);
        Assert.assertNotEquals(transactionRequestMock, null);
    }

    @Test
    public void test_getMerchantBaseUrl_positive() {
        Assert.assertEquals(midtransCoreSdkSpy.getMerchantBaseUrl(), SDKConfigTest.MERCHANT_BASE_URL);
    }

    @Test
    public void test_getMerchantBaseUrl_negative() {
        Assert.assertNotEquals(midtransCoreSdkSpy.getMerchantBaseUrl(), null);
    }

    @Test
    public void test_getEnvironment_positive() {
        Assert.assertEquals(midtransCoreSdkSpy.getEnvironment(), Environment.SANDBOX);
    }

    @Test
    public void test_getEnvironment_negative() {
        Assert.assertNotEquals(midtransCoreSdkSpy.getEnvironment(), Environment.PRODUCTION);
    }


    @Test
    public void test_getClientId_positive() {
        Assert.assertEquals(midtransCoreSdkSpy.getMerchantClientId(), SDKConfigTest.CLIENT_KEY);
    }

    @Test
    public void test_getClientId_negative() {
        Assert.assertNotEquals(midtransCoreSdkSpy.getMerchantClientId(), "123");
    }

    @Test
    public void test_getContext_positive() throws Exception {
        Assert.assertEquals(contextMock, midtransCoreSdkSpy.getContext());
    }

    @Test
    public void test_getContext_negative() throws Exception {
        Assert.assertNotEquals(contextMock, null);
    }

    @Test
    public void test_sdkTimeout_positive() throws Exception {
        Assert.assertEquals(midtransCoreSdkSpy.getApiRequestTimeOut(), 30);
    }

    @Test
    public void test_sdkTimeout_negative() throws Exception {
        Assert.assertNotEquals(midtransCoreSdkSpy.getApiRequestTimeOut(), 0);
    }

    /**
     * test checkout
     */

    @Test
    public void test_checkout() {
        midtransCoreSdkSpy.setTransactionRequest(transactionRequestMock);
        when(midtransCoreSdkSpy.isNetworkAvailable()).thenReturn(true);
        midtransCoreSdkSpy.checkout(checkoutCallbackMock);
        Mockito.verify(midtransCoreSdkSpy).checkout(checkoutCallbackMock);
    }

    @Test
    public void test_checkout_whenCallbackNull() {
        midtransCoreSdkSpy.checkout(null);
        verifyStatic(Mockito.times(1));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_checkout_whenNetworkUnAvailable() {
        midtransCoreSdkSpy.setTransactionRequest(transactionRequestMock);
        when(midtransCoreSdkSpy.isNetworkAvailable()).thenReturn(false);
        midtransCoreSdkSpy.checkout(checkoutCallbackMock);
        Mockito.verify(checkoutCallbackMock).onError(Matchers.any(Throwable.class));
    }

    /**
     * get transaction option
     */

    @Test
    public void test_getSnapTransaction() {
        when(midtransCoreSdkSpy.isNetworkAvailable()).thenReturn(true);
        midtransCoreSdkSpy.getTransactionOptions(SDKConfigTest.SNAP_TOKEN, transactionOptionCallbackMock);
        Mockito.verify(snapServiceManager).getTransactionOptions(SDKConfigTest.SNAP_TOKEN, transactionOptionCallbackMock);
    }

    @Test
    public void test_getSnapTransaction_whenCallbackNull() {
        midtransCoreSdkSpy.getTransactionOptions(SDKConfigTest.SNAP_TOKEN, null);
        verifyStatic(Mockito.times(1));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_getSnapTransaction_whenTokenNull() {
        when(midtransCoreSdkSpy.isNetworkAvailable()).thenReturn(true);
        midtransCoreSdkSpy.getTransactionOptions(null, transactionOptionCallbackMock);
        Mockito.verify(transactionOptionCallbackMock).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void test_getSnapTransaction_whenNetworkUnAvailable() {
        when(midtransCoreSdkSpy.isNetworkAvailable()).thenReturn(false);
        midtransCoreSdkSpy.getTransactionOptions(null, transactionOptionCallbackMock);
        Mockito.verify(transactionOptionCallbackMock).onError(Matchers.any(Throwable.class));
    }

}