package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.midtrans.sdk.corekit.SDKConfigTest;
import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.enums.Environment;
import com.midtrans.sdk.corekit.base.network.MidtransRestAdapter;
import com.midtrans.sdk.corekit.core.merchant.MerchantApiManager;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.TransactionRequest;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.response.CheckoutResponse;
import com.midtrans.sdk.corekit.core.snap.SnapApiManager;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.PaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.transaction.response.PaymentInfoResponse;
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
public class MidtransSdkTest {

    @Mock
    private Context contextMock;

    @Mock
    private ConnectivityManager connectivityManager;
    @Mock
    private NetworkInfo networkInfo;
    @Mock
    private MidtransCallback<CheckoutResponse> checkoutResponseMidtransCallback;
    @Mock
    private MidtransCallback<PaymentInfoResponse> paymentInfoResponseMidtransCallback;

    @Mock
    private MidtransSdk midtransSdkSpy;
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

        MidtransSdk midtransSDK = MidtransSdk.builder(contextMock,
                SDKConfigTest.CLIENT_KEY,
                SDKConfigTest.MERCHANT_BASE_URL)
                .setLogEnabled(true)
                .setEnvironment(Environment.SANDBOX)
                .build();

        midtransSdkSpy = Mockito.spy(midtransSDK);
    }

    @Test
    public void test_setTransactionRequest_positive() {
        midtransSdkSpy.setTransactionRequest(transactionRequestMock);
        Assert.assertEquals(transactionRequestMock, midtransSdkSpy.getTransactionRequest());
    }

    @Test
    public void test_setTransactionRequest_negative() {
        midtransSdkSpy.setTransactionRequest(transactionRequestMock);
        Assert.assertNotEquals(transactionRequestMock, null);
    }

    @Test
    public void test_getMerchantBaseUrl_positive() {
        Assert.assertEquals(midtransSdkSpy.getMerchantBaseUrl(), SDKConfigTest.MERCHANT_BASE_URL);
    }

    @Test
    public void test_getMerchantBaseUrl_negative() {
        Assert.assertNotEquals(midtransSdkSpy.getMerchantBaseUrl(), null);
    }

    @Test
    public void test_getEnvironment_positive() {
        Assert.assertEquals(midtransSdkSpy.getEnvironment(), Environment.SANDBOX);
    }

    @Test
    public void test_getEnvironment_negative() {
        Assert.assertNotEquals(midtransSdkSpy.getEnvironment(), Environment.PRODUCTION);
    }


    @Test
    public void test_getClientId_positive() {
        Assert.assertEquals(midtransSdkSpy.getMerchantClientId(), SDKConfigTest.CLIENT_KEY);
    }

    @Test
    public void test_getClientId_negative() {
        Assert.assertNotEquals(midtransSdkSpy.getMerchantClientId(), "123");
    }

    @Test
    public void test_getContext_positive() throws Exception {
        Assert.assertEquals(contextMock, midtransSdkSpy.getContext());
    }

    @Test
    public void test_getContext_negative() throws Exception {
        Assert.assertNotEquals(contextMock, null);
    }

    @Test
    public void test_sdkTimeout_positive() throws Exception {
        Assert.assertEquals(midtransSdkSpy.getApiRequestTimeOut(), 30);
    }

    @Test
    public void test_sdkTimeout_negative() throws Exception {
        Assert.assertNotEquals(midtransSdkSpy.getApiRequestTimeOut(), 0);
    }

    /**
     * test checkout
     */

    @Test
    public void test_checkout() {
        midtransSdkSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSdkSpy.isNetworkAvailable()).thenReturn(true);
        midtransSdkSpy.checkout(checkoutResponseMidtransCallback);
        Mockito.verify(midtransSdkSpy).checkout(checkoutResponseMidtransCallback);
    }

    @Test
    public void test_checkout_whenCallbackNull() {
        midtransSdkSpy.checkout(null);
        verifyStatic(Mockito.times(1));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_checkout_whenNetworkUnAvailable() {
        midtransSdkSpy.setTransactionRequest(transactionRequestMock);
        when(midtransSdkSpy.isNetworkAvailable()).thenReturn(false);
        midtransSdkSpy.checkout(checkoutResponseMidtransCallback);
        Mockito.verify(checkoutResponseMidtransCallback).onFailed(Matchers.any(Throwable.class));
    }

    /**
     * get transaction option
     */

    @Test
    public void test_getSnapTransaction() {
        when(midtransSdkSpy.isNetworkAvailable()).thenReturn(true);
        midtransSdkSpy.getPaymentInfo(SDKConfigTest.SNAP_TOKEN, paymentInfoResponseMidtransCallback);
        Mockito.verify(paymentInfoResponseMidtransCallback).onSuccess(Matchers.any(PaymentInfoResponse.class));
    }

    @Test
    public void test_getSnapTransaction_whenCallbackNull() {
        midtransSdkSpy.getPaymentInfo(SDKConfigTest.SNAP_TOKEN, null);
        verifyStatic(Mockito.times(1));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_getSnapTransaction_whenTokenNull() {
        when(midtransSdkSpy.isNetworkAvailable()).thenReturn(true);
        midtransSdkSpy.getPaymentInfo(null, paymentInfoResponseMidtransCallback);
        Mockito.verify(paymentInfoResponseMidtransCallback).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_getSnapTransaction_whenNetworkUnAvailable() {
        when(midtransSdkSpy.isNetworkAvailable()).thenReturn(false);
        midtransSdkSpy.getPaymentInfo(null, paymentInfoResponseMidtransCallback);
        Mockito.verify(paymentInfoResponseMidtransCallback).onFailed(Matchers.any(Throwable.class));
    }

}