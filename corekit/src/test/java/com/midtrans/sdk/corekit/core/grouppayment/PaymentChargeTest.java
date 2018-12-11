package com.midtrans.sdk.corekit.core.grouppayment;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.midtrans.sdk.corekit.SDKConfigTest;
import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.enums.Environment;
import com.midtrans.sdk.corekit.base.network.MidtransRestAdapter;
import com.midtrans.sdk.corekit.core.MidtransSdk;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.mandiriclick.MandiriClickpayParams;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.corekit.utilities.NetworkHelper;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.corekit.utilities.Validation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NetworkHelper.class, Looper.class, Utils.class, Log.class, TextUtils.class,
        Logger.class, MidtransRestAdapter.class, Validation.class, MidtransCallback.class})
public class PaymentChargeTest {

    @InjectMocks
    private BankTransferCharge bankTransferCharge;
    @InjectMocks
    private CardlessCreditCharge cardlessCreditCharge;
    @InjectMocks
    private DirectDebitCharge directDebitCharge;
    @InjectMocks
    private EWalletCharge eWalletCharge;
    @InjectMocks
    private OnlineDebitCharge onlineDebitCharge;
    @InjectMocks
    private StoreCharge storeCharge;

    @Mock
    private Context contextMock;
    @Mock
    private CustomerDetailPayRequest customerDetailPayRequest;
    @Mock
    private MidtransCallback<BasePaymentResponse> basePaymentResponseMidtransCallback;
    @Mock
    private String userId, customerNumber;
    @Mock
    private MandiriClickpayParams mandiriClickpayParams;
    @Before
    public void setup() {
        PowerMockito.mockStatic(MidtransCallback.class);
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Logger.class);
        PowerMockito.mockStatic(Looper.class);
        PowerMockito.mockStatic(Utils.class);
        PowerMockito.mockStatic(NetworkHelper.class);
        PowerMockito.mockStatic(MidtransRestAdapter.class);
        PowerMockito.mockStatic(BankTransferCharge.class);
        PowerMockito.mockStatic(Validation.class);

        MidtransSdk.builder(contextMock,
                SDKConfigTest.CLIENT_KEY,
                SDKConfigTest.MERCHANT_BASE_URL)
                .setLogEnabled(true)
                .setEnvironment(Environment.SANDBOX)
                .build();

        Mockito.when(TextUtils.isEmpty(Matchers.anyString())).thenReturn(false);
        Mockito.when(TextUtils.isEmpty(null)).thenReturn(true);
        Mockito.when(TextUtils.isEmpty("")).thenReturn(true);
    }

    @Test
    public void test_PaymentUsingBankTransferVaBca_negative() {
        bankTransferCharge.paymentUsingBankTransferVaBca(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, basePaymentResponseMidtransCallback);
        Mockito.verify(basePaymentResponseMidtransCallback).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_PaymentUsingBankTransferVaPermata_negative() {
        bankTransferCharge.paymentUsingBankTransferVaPermata(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, basePaymentResponseMidtransCallback);
        Mockito.verify(basePaymentResponseMidtransCallback).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_PaymentUsingBankTransferVaBni_negative() {
        bankTransferCharge.paymentUsingBankTransferVaBni(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, basePaymentResponseMidtransCallback);
        Mockito.verify(basePaymentResponseMidtransCallback).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_PaymentUsingCardlessCreditAkulaku_negative() {
        cardlessCreditCharge.paymentUsingAkulaku(SDKConfigTest.SNAP_TOKEN, basePaymentResponseMidtransCallback);
        Mockito.verify(basePaymentResponseMidtransCallback).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_PaymentUsingDirectDebitKlikBca_negative(){
        directDebitCharge.paymentUsingKlikBca(SDKConfigTest.SNAP_TOKEN,userId,basePaymentResponseMidtransCallback);
        Mockito.verify(basePaymentResponseMidtransCallback).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void testPaymentUsingDirectDebitMandiriClickPay(){
        directDebitCharge.paymentUsingMandiriClickPay(SDKConfigTest.SNAP_TOKEN,mandiriClickpayParams,basePaymentResponseMidtransCallback);
        Mockito.verify(basePaymentResponseMidtransCallback).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletTelkkomselCash_negative(){
        eWalletCharge.paymentUsingTelkomselCash(SDKConfigTest.SNAP_TOKEN,customerNumber,basePaymentResponseMidtransCallback);
        Mockito.verify(basePaymentResponseMidtransCallback).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletMandiriEcash_negative(){
        eWalletCharge.paymentUsingMandiriEcash(SDKConfigTest.SNAP_TOKEN,customerDetailPayRequest,basePaymentResponseMidtransCallback);
        Mockito.verify(basePaymentResponseMidtransCallback).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletGopay_negative(){
        eWalletCharge.paymentUsingGopay(SDKConfigTest.SNAP_TOKEN,customerNumber,basePaymentResponseMidtransCallback);
        Mockito.verify(basePaymentResponseMidtransCallback).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeCimbClicks_negative(){
        onlineDebitCharge.paymentUsingCimbClicks(SDKConfigTest.SNAP_TOKEN,basePaymentResponseMidtransCallback);
        Mockito.verify(basePaymentResponseMidtransCallback).onFailed(Matchers.any(Throwable.class));
    }
    @Test
    public void test_paymentUsingOnlineDebitChargeBcaClickPay_negative(){
        onlineDebitCharge.paymentUsingBcaClickPay(SDKConfigTest.SNAP_TOKEN,basePaymentResponseMidtransCallback);
        Mockito.verify(basePaymentResponseMidtransCallback).onFailed(Matchers.any(Throwable.class));
    }
    @Test
    public void test_paymentUsingOnlineDebitChargeBriEpay_negative(){
        onlineDebitCharge.paymentUsingBriEpay(SDKConfigTest.SNAP_TOKEN,basePaymentResponseMidtransCallback);
        Mockito.verify(basePaymentResponseMidtransCallback).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingStoreChange_negative(){
        storeCharge.paymentUsingIndomaret(SDKConfigTest.SNAP_TOKEN,basePaymentResponseMidtransCallback);
        Mockito.verify(basePaymentResponseMidtransCallback).onFailed(Matchers.any(Throwable.class));
    }

}