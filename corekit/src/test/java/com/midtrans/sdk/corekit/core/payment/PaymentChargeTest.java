package com.midtrans.sdk.corekit.core.payment;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.midtrans.sdk.corekit.MidtransSdk;
import com.midtrans.sdk.corekit.SDKConfigTest;
import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.enums.Environment;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.mandiriclick.MandiriClickpayParams;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.utilities.Helper;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.corekit.utilities.NetworkHelper;
import com.midtrans.sdk.corekit.utilities.ValidationHelper;

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

import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@SuppressWarnings("ALL")
@RunWith(PowerMockRunner.class)
@PrepareForTest({NetworkHelper.class,
        Looper.class,
        Helper.class,
        Log.class,
        TextUtils.class,
        Logger.class,
        ValidationHelper.class})
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
    private ConvenienceStoreCharge convenienceStoreCharge;

    @Mock
    private Context contextMock;
    @Mock
    private MidtransSdk midtransSdkMock;
    @Mock
    private Throwable throwable;
    @Mock
    private CustomerDetailPayRequest customerDetailPayRequest;
    @Mock
    private BasePaymentResponse responseMock;
    @Mock
    private MidtransCallback<BasePaymentResponse> callbackMock;
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
        PowerMockito.mockStatic(Helper.class);
        PowerMockito.mockStatic(NetworkHelper.class);
        PowerMockito.mockStatic(ValidationHelper.class);

        Mockito.when(TextUtils.isEmpty(Matchers.anyString())).thenReturn(false);
        Mockito.when(TextUtils.isEmpty(null)).thenReturn(true);
        Mockito.when(TextUtils.isEmpty("")).thenReturn(true);

        MidtransSdk midtransSdk = MidtransSdk.builder(contextMock,
                SDKConfigTest.CLIENT_KEY,
                SDKConfigTest.MERCHANT_BASE_URL)
                .setLogEnabled(true)
                .setEnvironment(Environment.SANDBOX)
                .build();

        midtransSdkMock = midtransSdk;
        callbackMock.onSuccess(responseMock);
        callbackMock.onFailed(throwable);
    }

    @Test
    public void test_paymentUsingBankTransferVaBca_positive() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(true);
        bankTransferCharge.paymentUsingBankTransferVaBca(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, callbackMock);
        Mockito.verify(callbackMock).onSuccess(Matchers.any(BasePaymentResponse.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaBca_negative_callback() {
        bankTransferCharge.paymentUsingBankTransferVaBca(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaBca_negative_snapTokenNull() {
        bankTransferCharge.paymentUsingBankTransferVaBca(null, customerDetailPayRequest, callbackMock);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingBankTransferVaBca_negative_callbackNull() {
        bankTransferCharge.paymentUsingBankTransferVaBca(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingBankTransferVaBca_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        bankTransferCharge.paymentUsingBankTransferVaBca(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaPermata_positive() {
        bankTransferCharge.paymentUsingBankTransferVaPermata(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, callbackMock);
        Mockito.verify(callbackMock).onSuccess(Matchers.any(BasePaymentResponse.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaPermata_negative_callback() {
        bankTransferCharge.paymentUsingBankTransferVaPermata(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaPermata_negative_snapTokenNull() {
        bankTransferCharge.paymentUsingBankTransferVaPermata(null, customerDetailPayRequest, callbackMock);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingBankTransferVaPermata_negative_callbackNull() {
        bankTransferCharge.paymentUsingBankTransferVaPermata(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingBankTransferVaPermata_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        bankTransferCharge.paymentUsingBankTransferVaPermata(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaBni_positive() {
        bankTransferCharge.paymentUsingBankTransferVaBni(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, callbackMock);
        Mockito.verify(callbackMock).onSuccess(Matchers.any(BasePaymentResponse.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaBni_negative_callback() {
        bankTransferCharge.paymentUsingBankTransferVaBni(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaBni_negative_snapTokenNull() {
        bankTransferCharge.paymentUsingBankTransferVaBni(null, customerDetailPayRequest, callbackMock);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingBankTransferVaBni_negative_callbackNull() {
        bankTransferCharge.paymentUsingBankTransferVaBni(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingBankTransferVaBni_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        bankTransferCharge.paymentUsingBankTransferVaBni(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingCardlessCreditAkulaku_positive() {
        cardlessCreditCharge.paymentUsingAkulaku(SDKConfigTest.SNAP_TOKEN, callbackMock);
        Mockito.verify(callbackMock).onSuccess(Matchers.any(BasePaymentResponse.class));
    }

    @Test
    public void test_paymentUsingCardlessCreditAkulaku_negative_callback() {
        cardlessCreditCharge.paymentUsingAkulaku(SDKConfigTest.SNAP_TOKEN, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingCardlessCreditAkulaku_negative_snapTokenNull() {
        cardlessCreditCharge.paymentUsingAkulaku(null, callbackMock);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingCardlessCreditAkulaku_negative_callbackNull() {
        cardlessCreditCharge.paymentUsingAkulaku(SDKConfigTest.SNAP_TOKEN, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingCardlessCreditAkulaku_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        cardlessCreditCharge.paymentUsingAkulaku(SDKConfigTest.SNAP_TOKEN, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingDirectDebitKlikBca_positive() {
        directDebitCharge.paymentUsingKlikBca(SDKConfigTest.SNAP_TOKEN, userId, callbackMock);
        Mockito.verify(callbackMock).onSuccess(Matchers.any(BasePaymentResponse.class));
    }

    @Test
    public void test_paymentUsingDirectDebitKlikBca_negative_callback() {
        directDebitCharge.paymentUsingKlikBca(SDKConfigTest.SNAP_TOKEN, userId, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingDirectDebitKlikBca_negative_snapTokenNull() {
        directDebitCharge.paymentUsingKlikBca(null, userId, callbackMock);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingDirectDebitKlikBca_negative_callbackNull() {
        directDebitCharge.paymentUsingKlikBca(SDKConfigTest.SNAP_TOKEN, userId, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingDirectDebitKlikBca_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        directDebitCharge.paymentUsingKlikBca(SDKConfigTest.SNAP_TOKEN, userId, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingDirectDebitMandiriClickPay_positive() {
        directDebitCharge.paymentUsingMandiriClickPay(SDKConfigTest.SNAP_TOKEN, mandiriClickpayParams, callbackMock);
        Mockito.verify(callbackMock).onSuccess(Matchers.any(BasePaymentResponse.class));
    }

    @Test
    public void test_paymentUsingDirectDebitMandiriClickPay_negative_callback() {
        directDebitCharge.paymentUsingMandiriClickPay(SDKConfigTest.SNAP_TOKEN, mandiriClickpayParams, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingDirectDebitMandiriClickPay_negative_snapTokenNull() {
        directDebitCharge.paymentUsingMandiriClickPay(null, mandiriClickpayParams, callbackMock);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingDirectDebitMandiriClickPay_negative_callbackNull() {
        directDebitCharge.paymentUsingMandiriClickPay(SDKConfigTest.SNAP_TOKEN, mandiriClickpayParams, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingDirectDebitMandiriClickPay_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        directDebitCharge.paymentUsingMandiriClickPay(SDKConfigTest.SNAP_TOKEN, mandiriClickpayParams, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletTelkkomselCash_positive() {
        eWalletCharge.paymentUsingTelkomselCash(SDKConfigTest.SNAP_TOKEN, customerNumber, callbackMock);
        Mockito.verify(callbackMock).onSuccess(Matchers.any(BasePaymentResponse.class));
    }

    @Test
    public void test_paymentUsingEwalletTelkkomselCash_negative_callback() {
        eWalletCharge.paymentUsingTelkomselCash(SDKConfigTest.SNAP_TOKEN, customerNumber, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletTelkkomselCash_negative_snapTokenNull() {
        eWalletCharge.paymentUsingTelkomselCash(null, customerNumber, callbackMock);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingEwalletTelkkomselCash_negative_callbackNull() {
        eWalletCharge.paymentUsingTelkomselCash(SDKConfigTest.SNAP_TOKEN, customerNumber, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingEwalletTelkkomselCash_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        eWalletCharge.paymentUsingTelkomselCash(SDKConfigTest.SNAP_TOKEN, customerNumber, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletMandiriEcash_positive() {
        eWalletCharge.paymentUsingMandiriEcash(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, callbackMock);
        Mockito.verify(callbackMock).onSuccess(Matchers.any(BasePaymentResponse.class));
    }

    @Test
    public void test_paymentUsingEwalletMandiriEcash_negative_callback() {
        eWalletCharge.paymentUsingMandiriEcash(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletMandiriEcash_negative_snapTokenNull() {
        eWalletCharge.paymentUsingMandiriEcash(null, customerDetailPayRequest, callbackMock);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingEwalletMandiriEcash_negative_callbackNull() {
        eWalletCharge.paymentUsingMandiriEcash(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingEwalletMandiriEcash_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        eWalletCharge.paymentUsingMandiriEcash(SDKConfigTest.SNAP_TOKEN, customerDetailPayRequest, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletGopay_positive() {
        eWalletCharge.paymentUsingGopay(SDKConfigTest.SNAP_TOKEN, customerNumber, callbackMock);
        Mockito.verify(callbackMock).onSuccess(Matchers.any(BasePaymentResponse.class));
    }

    @Test
    public void test_paymentUsingEwalletGopay_negative_callback() {
        eWalletCharge.paymentUsingGopay(SDKConfigTest.SNAP_TOKEN, customerNumber, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletGopay_negative_snapTokenNull() {
        eWalletCharge.paymentUsingGopay(null, customerNumber, callbackMock);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingEwalletGopay_negative_callbackNull() {
        eWalletCharge.paymentUsingGopay(SDKConfigTest.SNAP_TOKEN, customerNumber, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingEwalletGopay_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        eWalletCharge.paymentUsingGopay(SDKConfigTest.SNAP_TOKEN, customerNumber, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeCimbClicks_positive() {
        onlineDebitCharge.paymentUsingCimbClicks(SDKConfigTest.SNAP_TOKEN, callbackMock);
        Mockito.verify(callbackMock).onSuccess(Matchers.any(BasePaymentResponse.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeCimbClicks_negative_callback() {
        onlineDebitCharge.paymentUsingCimbClicks(SDKConfigTest.SNAP_TOKEN, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeCimbClicks_negative_snapTokenNull() {
        onlineDebitCharge.paymentUsingCimbClicks(null, callbackMock);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeCimbClicks_negative_callbackNull() {
        onlineDebitCharge.paymentUsingCimbClicks(SDKConfigTest.SNAP_TOKEN, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeCimbClicks_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        onlineDebitCharge.paymentUsingCimbClicks(SDKConfigTest.SNAP_TOKEN, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBcaClickPay_positive() {
        onlineDebitCharge.paymentUsingBcaClickPay(SDKConfigTest.SNAP_TOKEN, callbackMock);
        Mockito.verify(callbackMock).onSuccess(Matchers.any(BasePaymentResponse.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBcaClickPay_negative() {
        onlineDebitCharge.paymentUsingBcaClickPay(SDKConfigTest.SNAP_TOKEN, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBcaClickPay_negative_callback() {
        onlineDebitCharge.paymentUsingBcaClickPay(SDKConfigTest.SNAP_TOKEN, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBcaClickPay_negative_snapTokenNull() {
        onlineDebitCharge.paymentUsingBcaClickPay(null, callbackMock);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBcaClickPay_negative_callbackNull() {
        onlineDebitCharge.paymentUsingBcaClickPay(SDKConfigTest.SNAP_TOKEN, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBcaClickPay_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        onlineDebitCharge.paymentUsingBcaClickPay(SDKConfigTest.SNAP_TOKEN, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBriEpay_positive() {
        onlineDebitCharge.paymentUsingBriEpay(SDKConfigTest.SNAP_TOKEN, callbackMock);
        Mockito.verify(callbackMock).onSuccess(Matchers.any(BasePaymentResponse.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBriEpay_negative_callback() {
        onlineDebitCharge.paymentUsingBriEpay(SDKConfigTest.SNAP_TOKEN, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBriEpay_negative_snapTokenNull() {
        onlineDebitCharge.paymentUsingBriEpay(null, callbackMock);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBriEpay_negative_callbackNull() {
        onlineDebitCharge.paymentUsingBriEpay(SDKConfigTest.SNAP_TOKEN, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBriEpay_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        onlineDebitCharge.paymentUsingBriEpay(SDKConfigTest.SNAP_TOKEN, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingStoreChange_positive() {
        convenienceStoreCharge.paymentUsingIndomaret(SDKConfigTest.SNAP_TOKEN, callbackMock);
        Mockito.verify(callbackMock).onSuccess(Matchers.any(BasePaymentResponse.class));
    }

    @Test
    public void test_paymentUsingStoreChange_negative_callback() {
        convenienceStoreCharge.paymentUsingIndomaret(SDKConfigTest.SNAP_TOKEN, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingStoreChange_negative_snapTokenNull() {
        convenienceStoreCharge.paymentUsingIndomaret(null, callbackMock);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingStoreChange_negative_callbackNull() {
        convenienceStoreCharge.paymentUsingIndomaret(SDKConfigTest.SNAP_TOKEN, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingStoreChange_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        convenienceStoreCharge.paymentUsingIndomaret(SDKConfigTest.SNAP_TOKEN, callbackMock);
        Mockito.verify(callbackMock).onFailed(Matchers.any(Throwable.class));
    }

}