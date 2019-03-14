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
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.CreditCardPaymentParams;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.mandiriclick.MandiriClickpayParams;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.AkulakuResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.AlfamartPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaBankTransferReponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaKlikPayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BniBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BriEpayPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CimbClicksResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CreditCardResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.DanamonOnlineResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.GopayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.IndomaretPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.KlikBcaResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriBillResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriClickpayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriEcashResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.PermataBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.TelkomselCashResponse;
import com.midtrans.sdk.corekit.utilities.Helper;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.corekit.utilities.NetworkHelper;
import com.midtrans.sdk.corekit.utilities.ValidationHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import okhttp3.ResponseBody;

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

    private String exampleTextPositive, exampleTextNegative;

    @Mock
    private BankTransferCharge bankTransferCharge;
    @Mock
    private CardlessCreditCharge cardlessCreditCharge;
    @Mock
    private DirectDebitCharge directDebitCharge;
    @Mock
    private EWalletCharge eWalletCharge;
    @Mock
    private OnlineDebitCharge onlineDebitCharge;
    @Mock
    private ConvenienceStoreCharge convenienceStoreCharge;
    @Mock
    private CreditCardCharge creditCardCharge;

    @Mock
    private Context contextMock;
    @Mock
    private MidtransSdk midtransSdkMock;
    @Mock
    private Throwable throwable;
    @Mock
    private CustomerDetailPayRequest customerDetailPayRequest;
    @Mock
    private CreditCardPaymentParams creditCardPaymentParams;
    @Mock
    private String userId, customerNumber;
    @Mock
    private MandiriClickpayParams mandiriClickpayParams;

    @Mock
    private MandiriBillResponse responseMandiriBill;
    @Mock
    private MidtransCallback<MandiriBillResponse> callbackMandiriBill;
    @Mock
    private BcaBankTransferReponse responseBcaVaMock;
    @Mock
    private MidtransCallback<BcaBankTransferReponse> callbackBcaVaMock;
    @Mock
    private PermataBankTransferResponse responsePermataVaMock;
    @Mock
    private MidtransCallback<PermataBankTransferResponse> callbackPermataVaMock;
    @Mock
    private BniBankTransferResponse responseBniVaMock;
    @Mock
    private MidtransCallback<BniBankTransferResponse> callbackBniVaMock;
    @Mock
    private ResponseBody responseOtherVaMock;
    @Mock
    private MidtransCallback<ResponseBody> callbackOtherVaMock;
    @Mock
    private AkulakuResponse responseAkulakuMock;
    @Mock
    private MidtransCallback<AkulakuResponse> callbacAkulakuMock;
    @Mock
    private KlikBcaResponse responseKlikBca;
    @Mock
    private MidtransCallback<KlikBcaResponse> callbackKlikBca;
    @Mock
    private MandiriClickpayResponse responseMandiriClick;
    @Mock
    private MidtransCallback<MandiriClickpayResponse> callbackMandiriClick;
    @Mock
    private TelkomselCashResponse responseTelkomsel;
    @Mock
    private MidtransCallback<TelkomselCashResponse> callbackTelkomsel;
    @Mock
    private MandiriEcashResponse responseMandiriEcash;
    @Mock
    private MidtransCallback<MandiriEcashResponse> callbackMandiriEcash;
    @Mock
    private GopayResponse responseGopay;
    @Mock
    private MidtransCallback<GopayResponse> callbackGopay;
    @Mock
    private CimbClicksResponse responseCimb;
    @Mock
    private MidtransCallback<CimbClicksResponse> callbackCimb;
    @Mock
    private BcaKlikPayResponse responseBcaKlikpay;
    @Mock
    private MidtransCallback<BcaKlikPayResponse> callbackBcaKlikpay;
    @Mock
    private BriEpayPaymentResponse responseBriEpay;
    @Mock
    private MidtransCallback<BriEpayPaymentResponse> callbackBriEpay;
    @Mock
    private DanamonOnlineResponse responseDanamonOnline;
    @Mock
    private MidtransCallback<DanamonOnlineResponse> callbackDanamonOnline;
    @Mock
    private IndomaretPaymentResponse responseIndomaret;
    @Mock
    private MidtransCallback<IndomaretPaymentResponse> callbackIndomaret;
    @Mock
    private CreditCardResponse responseCreditCard;
    @Mock
    private MidtransCallback<CreditCardResponse> callbackCreditCard;
    @Mock
    private AlfamartPaymentResponse alfamartPaymentResponse;
    @Mock
    private MidtransCallback<AlfamartPaymentResponse> callbackAlfamart;

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
        callbackBcaVaMock.onSuccess(responseBcaVaMock);
        callbackBcaVaMock.onFailed(throwable);
        callbackPermataVaMock.onSuccess(responsePermataVaMock);
        callbackPermataVaMock.onFailed(throwable);
        callbackBniVaMock.onSuccess(responseBniVaMock);
        callbackBniVaMock.onFailed(throwable);
        callbackOtherVaMock.onSuccess(responseOtherVaMock);
        callbackOtherVaMock.onFailed(throwable);
        callbacAkulakuMock.onSuccess(responseAkulakuMock);
        callbacAkulakuMock.onFailed(throwable);
        callbackKlikBca.onSuccess(responseKlikBca);
        callbackKlikBca.onFailed(throwable);
        callbackMandiriClick.onSuccess(responseMandiriClick);
        callbackMandiriClick.onFailed(throwable);
        callbackTelkomsel.onSuccess(responseTelkomsel);
        callbackTelkomsel.onFailed(throwable);
        callbackMandiriEcash.onSuccess(responseMandiriEcash);
        callbackMandiriEcash.onFailed(throwable);
        callbackGopay.onSuccess(responseGopay);
        callbackGopay.onFailed(throwable);
        callbackCimb.onSuccess(responseCimb);
        callbackCimb.onFailed(throwable);
        callbackBcaKlikpay.onSuccess(responseBcaKlikpay);
        callbackBcaKlikpay.onFailed(throwable);
        callbackBriEpay.onSuccess(responseBriEpay);
        callbackBriEpay.onFailed(throwable);
        callbackIndomaret.onSuccess(responseIndomaret);
        callbackIndomaret.onFailed(throwable);
        callbackCreditCard.onSuccess(responseCreditCard);
        callbackCreditCard.onFailed(throwable);
        callbackDanamonOnline.onSuccess(responseDanamonOnline);
        callbackDanamonOnline.onFailed(throwable);
        callbackMandiriBill.onSuccess(responseMandiriBill);
        callbackMandiriBill.onFailed(throwable);
        callbackAlfamart.onSuccess(alfamartPaymentResponse);
        callbackAlfamart.onFailed(throwable);

        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
    }

    @Test
    public void test_paymentUsingBankTransferVaBca_positive() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(true);
        bankTransferCharge.paymentUsingBankTransferVaBca(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackBcaVaMock);
        Mockito.verify(callbackBcaVaMock).onSuccess(Matchers.any(BcaBankTransferReponse.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaBca_negative_callback() {
        bankTransferCharge.paymentUsingBankTransferVaBca(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackBcaVaMock);
        Mockito.verify(callbackBcaVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaBca_negative_snapTokenNull() {
        bankTransferCharge.paymentUsingBankTransferVaBca(null, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackBcaVaMock);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingBankTransferVaBca_negative_withoutParams() {
        bankTransferCharge.paymentUsingBankTransferVaBca(SDKConfigTest.SNAP_TOKEN, null, null, null, callbackBcaVaMock);
        Mockito.verify(callbackBcaVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaBca_negative_withoutSnapTken() {
        bankTransferCharge.paymentUsingBankTransferVaBca(null, exampleTextPositive, exampleTextPositive, exampleTextPositive, null);
        Mockito.verify(callbackBcaVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaBca_negative_callbackNull() {
        bankTransferCharge.paymentUsingBankTransferVaBca(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingBankTransferVaBca_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        bankTransferCharge.paymentUsingBankTransferVaBca(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackBcaVaMock);
        Mockito.verify(callbackBcaVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaPermata_positive() {
        bankTransferCharge.paymentUsingBankTransferVaPermata(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackPermataVaMock);
        Mockito.verify(callbackPermataVaMock).onSuccess(Matchers.any(PermataBankTransferResponse.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaPermata_negative_callback() {
        bankTransferCharge.paymentUsingBankTransferVaPermata(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackPermataVaMock);
        Mockito.verify(callbackPermataVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaPermata_negative_snapTokenNull() {
        bankTransferCharge.paymentUsingBankTransferVaPermata(null, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackPermataVaMock);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingBankTransferVaPermata_negative_callbackNull() {
        bankTransferCharge.paymentUsingBankTransferVaPermata(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingBankTransferVaPermata_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        bankTransferCharge.paymentUsingBankTransferVaPermata(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackPermataVaMock);
        Mockito.verify(callbackPermataVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaPermata_negative_withoutParams() {
        bankTransferCharge.paymentUsingBankTransferVaPermata(SDKConfigTest.SNAP_TOKEN, null, null, null, callbackPermataVaMock);
        Mockito.verify(callbackPermataVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaPermata_negative_withoutSnapTken() {
        bankTransferCharge.paymentUsingBankTransferVaPermata(null, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackPermataVaMock);
        Mockito.verify(callbackPermataVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaBni_positive() {
        bankTransferCharge.paymentUsingBankTransferVaBni(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackBniVaMock);
        Mockito.verify(callbackBniVaMock).onSuccess(Matchers.any(BniBankTransferResponse.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaBni_negative_callback() {
        bankTransferCharge.paymentUsingBankTransferVaBni(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackBniVaMock);
        Mockito.verify(callbackBniVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaBni_negative_snapTokenNull() {
        bankTransferCharge.paymentUsingBankTransferVaBni(null, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackBniVaMock);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingBankTransferVaBni_negative_callbackNull() {
        bankTransferCharge.paymentUsingBankTransferVaBni(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingBankTransferVaBni_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        bankTransferCharge.paymentUsingBankTransferVaBni(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackBniVaMock);
        Mockito.verify(callbackBniVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaBni_negative_withoutParams() {
        bankTransferCharge.paymentUsingBankTransferVaBni(SDKConfigTest.SNAP_TOKEN, null, null, null, callbackBniVaMock);
        Mockito.verify(callbackBniVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingBankTransferVaBni_negative_withoutSnapTken() {
        bankTransferCharge.paymentUsingBankTransferVaBni(null, exampleTextPositive, exampleTextPositive, exampleTextPositive, null);
        Mockito.verify(callbackBniVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOtherVa_positive() {
        bankTransferCharge.paymentUsingBankTransferVaOther(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackOtherVaMock);
        Mockito.verify(callbackOtherVaMock).onSuccess(Matchers.any(ResponseBody.class));
    }

    @Test
    public void test_paymentUsingOtherVa_negative_callback() {
        bankTransferCharge.paymentUsingBankTransferVaOther(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackOtherVaMock);
        Mockito.verify(callbackOtherVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOtherVa_negative_snapTokenNull() {
        bankTransferCharge.paymentUsingBankTransferVaOther(null, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackOtherVaMock);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingOtherVa_negative_callbackNull() {
        bankTransferCharge.paymentUsingBankTransferVaOther(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingOtherVa_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        bankTransferCharge.paymentUsingBankTransferVaOther(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackOtherVaMock);
        Mockito.verify(callbackOtherVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOtherVa_negative_withoutParams() {
        bankTransferCharge.paymentUsingBankTransferVaOther(SDKConfigTest.SNAP_TOKEN, null, null, null, callbackOtherVaMock);
        Mockito.verify(callbackOtherVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOtherVa_negative_withoutSnapTken() {
        bankTransferCharge.paymentUsingBankTransferVaOther(null, exampleTextPositive, exampleTextPositive, exampleTextPositive, null);
        Mockito.verify(callbackOtherVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentMandiriBillVa_positive() {
        bankTransferCharge.paymentUsingBankTransferVaMandiriBill(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackMandiriBill);
        Mockito.verify(callbackOtherVaMock).onSuccess(Matchers.any(ResponseBody.class));
    }

    @Test
    public void test_paymentUMandiriBillVa_negative_callback() {
        bankTransferCharge.paymentUsingBankTransferVaMandiriBill(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackMandiriBill);
        Mockito.verify(callbackOtherVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingMandiriBillVa_negative_snapTokenNull() {
        bankTransferCharge.paymentUsingBankTransferVaMandiriBill(null, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackMandiriBill);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingMandiriBillVa_negative_callbackNull() {
        bankTransferCharge.paymentUsingBankTransferVaMandiriBill(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingMandiriBillVa_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        bankTransferCharge.paymentUsingBankTransferVaMandiriBill(SDKConfigTest.SNAP_TOKEN, exampleTextPositive, exampleTextPositive, exampleTextPositive, callbackMandiriBill);
        Mockito.verify(callbackOtherVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingMandiriBillVa_negative_withoutParams() {
        bankTransferCharge.paymentUsingBankTransferVaMandiriBill(SDKConfigTest.SNAP_TOKEN, null, null, null, callbackMandiriBill);
        Mockito.verify(callbackOtherVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingMandiriBillVa_negative_withoutSnapTken() {
        bankTransferCharge.paymentUsingBankTransferVaMandiriBill(null, exampleTextPositive, exampleTextPositive, exampleTextPositive, null);
        Mockito.verify(callbackOtherVaMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingCardlessCreditAkulaku_positive() {
        cardlessCreditCharge.paymentUsingAkulaku(SDKConfigTest.SNAP_TOKEN, callbacAkulakuMock);
        Mockito.verify(callbacAkulakuMock).onSuccess(Matchers.any(AkulakuResponse.class));
    }

    @Test
    public void test_paymentUsingCardlessCreditAkulaku_negative_callback() {
        cardlessCreditCharge.paymentUsingAkulaku(SDKConfigTest.SNAP_TOKEN, callbacAkulakuMock);
        Mockito.verify(callbacAkulakuMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingCardlessCreditAkulaku_negative_snapTokenNull() {
        cardlessCreditCharge.paymentUsingAkulaku(null, callbacAkulakuMock);
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
        cardlessCreditCharge.paymentUsingAkulaku(SDKConfigTest.SNAP_TOKEN, callbacAkulakuMock);
        Mockito.verify(callbacAkulakuMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingCardlessCreditAkulaku_negative_withoutSnapToken() {
        cardlessCreditCharge.paymentUsingAkulaku(null, callbacAkulakuMock);
        Mockito.verify(callbacAkulakuMock).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingDirectDebitKlikBca_positive() {
        directDebitCharge.paymentUsingKlikBca(SDKConfigTest.SNAP_TOKEN, userId, callbackKlikBca);
        Mockito.verify(callbackKlikBca).onSuccess(Matchers.any(KlikBcaResponse.class));
    }

    @Test
    public void test_paymentUsingDirectDebitKlikBca_negative_callback() {
        directDebitCharge.paymentUsingKlikBca(SDKConfigTest.SNAP_TOKEN, userId, callbackKlikBca);
        Mockito.verify(callbackKlikBca).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingDirectDebitKlikBca_negative_snapTokenNull() {
        directDebitCharge.paymentUsingKlikBca(null, userId, callbackKlikBca);
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
        directDebitCharge.paymentUsingKlikBca(SDKConfigTest.SNAP_TOKEN, userId, callbackKlikBca);
        Mockito.verify(callbackKlikBca).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingDirectDebitKlikBca_negative_withoutParams() {
        directDebitCharge.paymentUsingKlikBca(SDKConfigTest.SNAP_TOKEN, null, callbackKlikBca);
        Mockito.verify(callbackKlikBca).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingDirectDebitKlikBca_negative_withoutSnapTken() {
        directDebitCharge.paymentUsingKlikBca(null, userId, null);
        Mockito.verify(callbackKlikBca).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingDirectDebitMandiriClickPay_positive() {
        directDebitCharge.paymentUsingMandiriClickPay(SDKConfigTest.SNAP_TOKEN, exampleTextNegative, exampleTextNegative, exampleTextNegative, callbackMandiriClick);
        Mockito.verify(callbackMandiriClick).onSuccess(Matchers.any(MandiriClickpayResponse.class));
    }

    @Test
    public void test_paymentUsingDirectDebitMandiriClickPay_negative_callback() {
        directDebitCharge.paymentUsingMandiriClickPay(SDKConfigTest.SNAP_TOKEN, exampleTextNegative, exampleTextNegative, exampleTextNegative, callbackMandiriClick);
        Mockito.verify(callbackMandiriClick).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingDirectDebitMandiriClickPay_negative_snapTokenNull() {
        directDebitCharge.paymentUsingMandiriClickPay(null, exampleTextNegative, exampleTextNegative, exampleTextNegative, callbackMandiriClick);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingDirectDebitMandiriClickPay_negative_callbackNull() {
        directDebitCharge.paymentUsingMandiriClickPay(SDKConfigTest.SNAP_TOKEN, exampleTextNegative, exampleTextNegative, exampleTextNegative, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingDirectDebitMandiriClickPay_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        directDebitCharge.paymentUsingMandiriClickPay(SDKConfigTest.SNAP_TOKEN, exampleTextNegative, exampleTextNegative, exampleTextNegative, callbackMandiriClick);
        Mockito.verify(callbackMandiriClick).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingDirectDebitMandiriClickpay_negative_withoutParams() {
        directDebitCharge.paymentUsingMandiriClickPay(SDKConfigTest.SNAP_TOKEN, null, null, null, callbackMandiriClick);
        Mockito.verify(callbackMandiriClick).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingDirectDebitMandiriClickpay_negative_withoutSnapTken() {
        directDebitCharge.paymentUsingMandiriClickPay(null, exampleTextNegative, exampleTextNegative, exampleTextNegative, null);
        Mockito.verify(callbackMandiriClick).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletTelkkomselCash_positive() {
        eWalletCharge.paymentUsingTelkomselCash(SDKConfigTest.SNAP_TOKEN, customerNumber, callbackTelkomsel);
        Mockito.verify(callbackTelkomsel).onSuccess(Matchers.any(TelkomselCashResponse.class));
    }

    @Test
    public void test_paymentUsingEwalletTelkkomselCash_negative_callback() {
        eWalletCharge.paymentUsingTelkomselCash(SDKConfigTest.SNAP_TOKEN, customerNumber, callbackTelkomsel);
        Mockito.verify(callbackTelkomsel).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletTelkkomselCash_negative_snapTokenNull() {
        eWalletCharge.paymentUsingTelkomselCash(null, customerNumber, callbackTelkomsel);
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
        eWalletCharge.paymentUsingTelkomselCash(SDKConfigTest.SNAP_TOKEN, customerNumber, callbackTelkomsel);
        Mockito.verify(callbackTelkomsel).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletTelkkomselCash_negative_withoutParams() {
        eWalletCharge.paymentUsingTelkomselCash(SDKConfigTest.SNAP_TOKEN, null, callbackTelkomsel);
        Mockito.verify(callbackTelkomsel).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletTelkkomselCash_negative_withoutSnapTken() {
        eWalletCharge.paymentUsingTelkomselCash(null, customerNumber, null);
        Mockito.verify(callbackTelkomsel).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletMandiriEcash_positive() {
        eWalletCharge.paymentUsingMandiriEcash(SDKConfigTest.SNAP_TOKEN, callbackMandiriEcash);
        Mockito.verify(callbackMandiriEcash).onSuccess(Matchers.any(MandiriEcashResponse.class));
    }

    @Test
    public void test_paymentUsingEwalletMandiriEcash_negative_callback() {
        eWalletCharge.paymentUsingMandiriEcash(SDKConfigTest.SNAP_TOKEN, callbackMandiriEcash);
        Mockito.verify(callbackMandiriEcash).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletMandiriEcash_negative_snapTokenNull() {
        eWalletCharge.paymentUsingMandiriEcash(null,  callbackMandiriEcash);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingEwalletMandiriEcash_negative_callbackNull() {
        eWalletCharge.paymentUsingMandiriEcash(SDKConfigTest.SNAP_TOKEN, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingEwalletMandiriEcash_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        eWalletCharge.paymentUsingMandiriEcash(SDKConfigTest.SNAP_TOKEN,  callbackMandiriEcash);
        Mockito.verify(callbackMandiriEcash).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletMandiriEcash_negative_withoutParams() {
        eWalletCharge.paymentUsingMandiriEcash(SDKConfigTest.SNAP_TOKEN,  callbackMandiriEcash);
        Mockito.verify(callbackMandiriEcash).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletMandiriEcash_negative_withoutSnapTken() {
        eWalletCharge.paymentUsingMandiriEcash(null, null);
        Mockito.verify(callbackMandiriEcash).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletGopay_positive() {
        eWalletCharge.paymentUsingGopay(SDKConfigTest.SNAP_TOKEN, callbackGopay);
        Mockito.verify(callbackGopay).onSuccess(Matchers.any(GopayResponse.class));
    }

    @Test
    public void test_paymentUsingEwalletGopay_negative_callback() {
        eWalletCharge.paymentUsingGopay(SDKConfigTest.SNAP_TOKEN, callbackGopay);
        Mockito.verify(callbackGopay).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletGopay_negative_snapTokenNull() {
        eWalletCharge.paymentUsingGopay(null, callbackGopay);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingEwalletGopay_negative_callbackNull() {
        eWalletCharge.paymentUsingGopay(SDKConfigTest.SNAP_TOKEN, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingEwalletGopay_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        eWalletCharge.paymentUsingGopay(SDKConfigTest.SNAP_TOKEN, callbackGopay);
        Mockito.verify(callbackGopay).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingEwalletGopay_negative_withoutSnapTken() {
        eWalletCharge.paymentUsingGopay(null, null);
        Mockito.verify(callbackGopay).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeCimbClicks_positive() {
        onlineDebitCharge.paymentUsingCimbClicks(SDKConfigTest.SNAP_TOKEN, callbackCimb);
        Mockito.verify(callbackCimb).onSuccess(Matchers.any(CimbClicksResponse.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeCimbClicks_negative_callback() {
        onlineDebitCharge.paymentUsingCimbClicks(SDKConfigTest.SNAP_TOKEN, callbackCimb);
        Mockito.verify(callbackCimb).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeCimbClicks_negative_snapTokenNull() {
        onlineDebitCharge.paymentUsingCimbClicks(null, callbackCimb);
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
        onlineDebitCharge.paymentUsingCimbClicks(SDKConfigTest.SNAP_TOKEN, callbackCimb);
        Mockito.verify(callbackCimb).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeCimbClicks_negative_withoutSnapToken() {
        onlineDebitCharge.paymentUsingCimbClicks(null, callbackCimb);
        Mockito.verify(callbackCimb).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBcaClickPay_positive() {
        onlineDebitCharge.paymentUsingBcaKlikpay(SDKConfigTest.SNAP_TOKEN, callbackBcaKlikpay);
        Mockito.verify(callbackBcaKlikpay).onSuccess(Matchers.any(BcaKlikPayResponse.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBcaClickPay_negative() {
        onlineDebitCharge.paymentUsingBcaKlikpay(SDKConfigTest.SNAP_TOKEN, callbackBcaKlikpay);
        Mockito.verify(callbackBcaKlikpay).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBcaClickPay_negative_callback() {
        onlineDebitCharge.paymentUsingBcaKlikpay(SDKConfigTest.SNAP_TOKEN, callbackBcaKlikpay);
        Mockito.verify(callbackBcaKlikpay).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBcaClickPay_negative_snapTokenNull() {
        onlineDebitCharge.paymentUsingBcaKlikpay(null, callbackBcaKlikpay);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBcaClickPay_negative_callbackNull() {
        onlineDebitCharge.paymentUsingBcaKlikpay(SDKConfigTest.SNAP_TOKEN, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBcaClickPay_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        onlineDebitCharge.paymentUsingBcaKlikpay(SDKConfigTest.SNAP_TOKEN, callbackBcaKlikpay);
        Mockito.verify(callbackBcaKlikpay).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBcaClickPay_negative_withoutSnapToken() {
        onlineDebitCharge.paymentUsingBcaKlikpay(null, callbackBcaKlikpay);
        Mockito.verify(callbackBcaKlikpay).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeDanamonOnline_positive() {
        onlineDebitCharge.paymentUsingDanamonOnline(SDKConfigTest.SNAP_TOKEN, callbackDanamonOnline);
        Mockito.verify(callbackDanamonOnline).onSuccess(Matchers.any(DanamonOnlineResponse.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeDanamonOnline_negative() {
        onlineDebitCharge.paymentUsingDanamonOnline(SDKConfigTest.SNAP_TOKEN, callbackDanamonOnline);
        Mockito.verify(callbackDanamonOnline).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeDanamonOnline_negative_callback() {
        onlineDebitCharge.paymentUsingDanamonOnline(SDKConfigTest.SNAP_TOKEN, callbackDanamonOnline);
        Mockito.verify(callbackDanamonOnline).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeDanamonOnline_negative_snapTokenNull() {
        onlineDebitCharge.paymentUsingDanamonOnline(null, callbackDanamonOnline);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeDanamonOnline_negative_callbackNull() {
        onlineDebitCharge.paymentUsingDanamonOnline(SDKConfigTest.SNAP_TOKEN, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeDanamonOnline_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        onlineDebitCharge.paymentUsingDanamonOnline(SDKConfigTest.SNAP_TOKEN, callbackDanamonOnline);
        Mockito.verify(callbackDanamonOnline).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeDanamonOnline_negative_withoutSnapToken() {
        onlineDebitCharge.paymentUsingDanamonOnline(null, callbackDanamonOnline);
        Mockito.verify(callbackDanamonOnline).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBriEpay_positive() {
        onlineDebitCharge.paymentUsingBriEpay(SDKConfigTest.SNAP_TOKEN, callbackBriEpay);
        Mockito.verify(callbackBriEpay).onSuccess(Matchers.any(BriEpayPaymentResponse.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBriEpay_negative_callback() {
        onlineDebitCharge.paymentUsingBriEpay(SDKConfigTest.SNAP_TOKEN, callbackBriEpay);
        Mockito.verify(callbackBriEpay).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBriEpay_negative_snapTokenNull() {
        onlineDebitCharge.paymentUsingBriEpay(null, callbackBriEpay);
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
        onlineDebitCharge.paymentUsingBriEpay(SDKConfigTest.SNAP_TOKEN, callbackBriEpay);
        Mockito.verify(callbackBriEpay).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingOnlineDebitChargeBriEpay_negative_withoutSnapTken() {
        onlineDebitCharge.paymentUsingBriEpay(null, callbackBriEpay);
        Mockito.verify(callbackBriEpay).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingStoreIndomaret_positive() {
        convenienceStoreCharge.paymentUsingIndomaret(SDKConfigTest.SNAP_TOKEN, callbackIndomaret);
        Mockito.verify(callbackIndomaret).onSuccess(Matchers.any(IndomaretPaymentResponse.class));
    }

    @Test
    public void test_paymentUsingStoreIndomaret_negative_callback() {
        convenienceStoreCharge.paymentUsingIndomaret(SDKConfigTest.SNAP_TOKEN, callbackIndomaret);
        Mockito.verify(callbackIndomaret).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingStoreIndomaret_negative_snapTokenNull() {
        convenienceStoreCharge.paymentUsingIndomaret(null, callbackIndomaret);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingStoreIndomaret_negative_callbackNull() {
        convenienceStoreCharge.paymentUsingIndomaret(SDKConfigTest.SNAP_TOKEN, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingStoreIndomaret_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        convenienceStoreCharge.paymentUsingIndomaret(SDKConfigTest.SNAP_TOKEN, callbackIndomaret);
        Mockito.verify(callbackIndomaret).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingStoreIndomaret_negative_withoutSnapToken() {
        convenienceStoreCharge.paymentUsingIndomaret(null, callbackIndomaret);
        Mockito.verify(callbackIndomaret).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingStoreAlfamart_positive() {
        convenienceStoreCharge.paymentUsingAlfamart(SDKConfigTest.SNAP_TOKEN, callbackAlfamart);
        Mockito.verify(callbackIndomaret).onSuccess(Matchers.any(IndomaretPaymentResponse.class));
    }

    @Test
    public void test_paymentUsingStoreAlfamart_negative_callback() {
        convenienceStoreCharge.paymentUsingAlfamart(SDKConfigTest.SNAP_TOKEN, callbackAlfamart);
        Mockito.verify(callbackIndomaret).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingStoreAlfamart_negative_snapTokenNull() {
        convenienceStoreCharge.paymentUsingAlfamart(null, callbackAlfamart);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingStoreAlfamart_negative_callbackNull() {
        convenienceStoreCharge.paymentUsingAlfamart(SDKConfigTest.SNAP_TOKEN, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingStoreAlfamart_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        convenienceStoreCharge.paymentUsingAlfamart(SDKConfigTest.SNAP_TOKEN, callbackAlfamart);
        Mockito.verify(callbackIndomaret).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingStoreAlfamart_negative_withoutSnapToken() {
        convenienceStoreCharge.paymentUsingAlfamart(null, callbackAlfamart);
        Mockito.verify(callbackIndomaret).onFailed(Matchers.any(Throwable.class));
    }

    /*@Test
    public void test_paymentUsingCreditCard_positive() {
        creditCardCharge.paymentUsingCard(SDKConfigTest.SNAP_TOKEN, creditCardPaymentParams, customerDetailPayRequest, callbackCreditCard);
        Mockito.verify(callbackCreditCard).onSuccess(Matchers.any(CreditCardResponse.class));
    }

    @Test
    public void test_paymentUsingCreditCard_negative_callback() {
        creditCardCharge.paymentUsingCard(SDKConfigTest.SNAP_TOKEN, creditCardPaymentParams, customerDetailPayRequest, callbackCreditCard);
        Mockito.verify(callbackCreditCard).onFailed(Matchers.any(Throwable.class));
    }

    @Test
    public void test_paymentUsingCreditCard_negative_snapTokenNull() {
        creditCardCharge.paymentUsingCard(null, creditCardPaymentParams, customerDetailPayRequest, callbackCreditCard);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingCreditCard_negative_callbackNull() {
        creditCardCharge.paymentUsingCard(SDKConfigTest.SNAP_TOKEN, creditCardPaymentParams, customerDetailPayRequest, null);
        verifyStatic(Mockito.times(0));
        Logger.error(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void test_paymentUsingCreditCard_negative_noNetwork() {
        when(NetworkHelper.isNetworkAvailable(midtransSdkMock.getContext())).thenReturn(false);
        creditCardCharge.paymentUsingCard(SDKConfigTest.SNAP_TOKEN, creditCardPaymentParams, customerDetailPayRequest, callbackCreditCard);
        Mockito.verify(callbackCreditCard).onFailed(Matchers.any(Throwable.class));
    }*/

}