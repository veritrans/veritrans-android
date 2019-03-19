package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.midtrans.sdk.corekit.MidtransSdk;
import com.midtrans.sdk.corekit.SDKConfigTest;
import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.enums.Environment;
import com.midtrans.sdk.corekit.core.api.merchant.MerchantApiManager;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.response.CheckoutWithTransactionResponse;
import com.midtrans.sdk.corekit.core.api.snap.SnapApiManager;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.corekit.utilities.NetworkHelper;

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

import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NetworkHelper.class,
        Looper.class,
        TextUtils.class,
        Log.class,
        Logger.class})
public class MidtransSdkTest {

    @Mock
    private Context contextMock;
    @Mock
    private ConnectivityManager connectivityManagerMock;
    @Mock
    private NetworkInfo networkInfoMock;
    @Mock
    private MidtransSdk midtransSdkMock;

    // Mock Callback
    @Mock
    private MidtransCallback<CheckoutWithTransactionResponse> checkoutCallbackMock;
    @Mock
    private MidtransCallback<PaymentInfoResponse> infoCallbackMock;

    // Mock Model
    @Mock
    private CheckoutTransaction checkoutTransactionMock;
    @Mock
    private PaymentInfoResponse paymentInfoResponse;
    @Mock
    private CheckoutWithTransactionResponse checkoutWithTransactionResponse;

    // Mock Api Manager
    @Mock
    private SnapApiManager snapServiceManager;
    @Mock
    private MerchantApiManager merchantServiceManager;

    @Before
    public void setup() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Logger.class);
        PowerMockito.mockStatic(Looper.class);
        PowerMockito.mockStatic(NetworkHelper.class);

        Mockito.when(contextMock.getApplicationContext()).thenReturn(contextMock);
        Mockito.when(contextMock.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManagerMock);
        Mockito.when(connectivityManagerMock.getActiveNetworkInfo()).thenReturn(networkInfoMock);
        Mockito.when(networkInfoMock.isConnected()).thenReturn(false);
        Mockito.when(midtransSdkMock.getApiRequestTimeOut()).thenReturn(30);
        Mockito.when(TextUtils.isEmpty(Matchers.anyString())).thenReturn(false);
        Mockito.when(TextUtils.isEmpty(null)).thenReturn(true);
        Mockito.when(TextUtils.isEmpty("")).thenReturn(true);
        Mockito.when(NetworkHelper.newSnapServiceManager(SDKConfigTest.MERCHANT_BASE_URL, SDKConfigTest.TIME_OUT)).thenReturn(snapServiceManager);
        Mockito.when(NetworkHelper.newMerchantServiceManager(SDKConfigTest.MERCHANT_BASE_URL, SDKConfigTest.TIME_OUT)).thenReturn(merchantServiceManager);

        MidtransSdk midtransSDK = MidtransSdk.builder(contextMock,
                SDKConfigTest.CLIENT_KEY,
                SDKConfigTest.MERCHANT_BASE_URL)
                .setLogEnabled(true)
                .setEnvironment(Environment.SANDBOX)
                .setApiRequestTimeOut(60)
                .build();

        infoCallbackMock.onSuccess(paymentInfoResponse);
        infoCallbackMock.onFailed(new Throwable());
        checkoutCallbackMock.onSuccess(checkoutWithTransactionResponse);
        checkoutCallbackMock.onFailed(new Throwable());

        midtransSdkMock = Mockito.spy(midtransSDK);
    }

    @Test
    public void test_setTransactionRequest_positive() {
        midtransSdkMock.setCheckoutTransaction(checkoutTransactionMock);
        Assert.assertEquals(checkoutTransactionMock, midtransSdkMock.getCheckoutTransaction());
    }

    @Test
    public void test_setTransactionRequest_negative() {
        midtransSdkMock.setCheckoutTransaction(checkoutTransactionMock);
        Assert.assertNotEquals(checkoutTransactionMock, null);
    }

    @Test
    public void test_getMerchantBaseUrl_positive() {
        Assert.assertEquals(midtransSdkMock.getMerchantBaseUrl(), SDKConfigTest.MERCHANT_BASE_URL);
    }

    @Test
    public void test_getMerchantBaseUrl_negative() {
        Assert.assertNotEquals(midtransSdkMock.getMerchantBaseUrl(), null);
    }

    @Test
    public void test_getEnvironment_positive() {
        Assert.assertEquals(midtransSdkMock.getEnvironment(), Environment.SANDBOX);
    }

    @Test
    public void test_getEnvironment_negative() {
        Assert.assertNotEquals(midtransSdkMock.getEnvironment(), Environment.PRODUCTION);
    }

    @Test
    public void test_getClientId_positive() {
        Assert.assertEquals(midtransSdkMock.getMerchantClientId(), SDKConfigTest.CLIENT_KEY);
    }

    @Test
    public void test_getClientId_negative() {
        Assert.assertNotEquals(midtransSdkMock.getMerchantClientId(), "123");
    }

    @Test
    public void test_getContext_positive() {
        Assert.assertEquals(contextMock, midtransSdkMock.getContext());
    }

    @Test
    public void test_getContext_negative() {
        Assert.assertNotEquals(contextMock, null);
    }

    @Test
    public void test_sdkTimeout_positive() {
        Assert.assertEquals(midtransSdkMock.getApiRequestTimeOut(), 60);
    }

    @Test
    public void test_sdkTimeout_negative() {
        Assert.assertNotEquals(midtransSdkMock.getApiRequestTimeOut(), 30);
    }

    /**
     * test checkoutWithTransaction
     */

    /*@Test
    public void test_checkout_positive_perform() {
        midtransSdkMock.setCheckoutTransaction(checkoutTransactionMock);
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(true);
        midtransSdkMock.checkoutWithTransaction(checkoutCallbackMock);
        Mockito.verify(midtransSdkMock).checkoutWithTransaction(checkoutCallbackMock);
    }*/

    @Test
    public void test_checkout_positive_callback() {
        midtransSdkMock.setCheckoutTransaction(checkoutTransactionMock);
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(true);
        Mockito.verify(checkoutCallbackMock).onSuccess(any(CheckoutWithTransactionResponse.class));
    }

    /*@Test
    public void test_checkout_negative_whenCallbackNull() {
        midtransSdkMock.checkoutWithTransaction(null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }*/

    @Test
    public void test_checkout_negative_whenNetworkUnAvailable() {
        midtransSdkMock.setCheckoutTransaction(checkoutTransactionMock);
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        Mockito.verify(checkoutCallbackMock).onFailed(any(Throwable.class));
    }

    /**
     * get transaction option
     */

    @Test
    public void test_getSnapTransaction_positive_perform() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(true);
        midtransSdkMock.getPaymentInfo(SDKConfigTest.SNAP_TOKEN, infoCallbackMock);
        Mockito.verify(midtransSdkMock).getPaymentInfo(SDKConfigTest.SNAP_TOKEN, infoCallbackMock);
    }

    @Test
    public void test_getSnapTransaction_positive_callback() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(true);
        midtransSdkMock.getPaymentInfo(SDKConfigTest.SNAP_TOKEN, infoCallbackMock);
        Mockito.verify(infoCallbackMock).onSuccess(any(PaymentInfoResponse.class));
    }

    @Test
    public void test_getSnapTransaction_negative_whenCallbackNull() {
        midtransSdkMock.getPaymentInfo(SDKConfigTest.SNAP_TOKEN, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_getSnapTransaction_negative_whenSnapTokenNull() {
        midtransSdkMock.getPaymentInfo(null, infoCallbackMock);
        Mockito.verify(infoCallbackMock).onFailed(any(Throwable.class));
    }

    @Test
    public void test_getSnapTransaction_negative_whenTokenNull() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(true);
        midtransSdkMock.getPaymentInfo(null, infoCallbackMock);
        Mockito.verify(infoCallbackMock).onFailed(any(Throwable.class));
    }

    @Test
    public void test_getSnapTransaction_negative_whenNetworkUnAvailable() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        midtransSdkMock.getPaymentInfo(null, infoCallbackMock);
        Mockito.verify(infoCallbackMock).onFailed(any(Throwable.class));
    }
}