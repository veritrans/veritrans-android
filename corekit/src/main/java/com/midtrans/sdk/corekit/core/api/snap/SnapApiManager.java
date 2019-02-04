package com.midtrans.sdk.corekit.core.api.snap;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.base.network.BaseServiceManager;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.BasePaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.PaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.CreditCardPaymentParams;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.CreditCardPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.gopay.GopayPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.klikbca.KlikBcaPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.mandiriclick.MandiriClickpayParams;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.mandiriclick.MandiriClickpayPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.telkomsel.TelkomselCashPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaBankTransferReponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BniBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.PermataBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.AkulakuResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.IndomaretPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CreditCardResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.KlikBcaResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriClickpayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.GopayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriEcashResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.TelkomselCashResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaKlikpayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BriEpayPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CimbClicksResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.DanamonOnlineResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.point.PointResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class SnapApiManager extends BaseServiceManager {

    private static final String TAG = "SnapApiManager";

    private SnapApiService apiService;

    public SnapApiManager(SnapApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * This method will create a HTTP request to Snap to get transaction option.
     *
     * @param snapToken Snap token after creating Snap Token from Merchant Server.
     * @param callback  callback of Transaction Option.
     */
    public void getPaymentInfo(final String snapToken,
                               final MidtransCallback<PaymentInfoResponse> callback) {

        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            Call<PaymentInfoResponse> call = apiService.getTransactionOptions(snapToken);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Bank Transfer BCA
     *
     * @param snapToken       snapToken after get payment info.
     * @param customerDetails Payment Details.
     * @param callback        Transaction callback.
     */
    public void paymentUsingBankTransferVaBca(final String snapToken,
                                              final CustomerDetailPayRequest customerDetails,
                                              final MidtransCallback<BcaBankTransferReponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            PaymentRequest paymentRequest = new PaymentRequest(PaymentType.BCA_VA, customerDetails);
            Call<BcaBankTransferReponse> call = apiService.paymentBankTransferBca(snapToken, paymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Bank Transfer BNI
     *
     * @param snapToken       snapToken after get payment info.
     * @param customerDetails Payment Details.
     * @param callback        Transaction callback.
     */
    public void paymentUsingBankTransferVaBni(final String snapToken,
                                              final CustomerDetailPayRequest customerDetails,
                                              final MidtransCallback<BniBankTransferResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            PaymentRequest paymentRequest = new PaymentRequest(PaymentType.BNI_VA, customerDetails);
            Call<BniBankTransferResponse> call = apiService.paymentBankTransferBni(snapToken, paymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Bank Transfer Permata
     *
     * @param snapToken       snapToken after get payment info.
     * @param customerDetails Payment Details.
     * @param callback        Transaction callback.
     */
    public void paymentUsingBankTransferVaPermata(final String snapToken,
                                                  final CustomerDetailPayRequest customerDetails,
                                                  final MidtransCallback<PermataBankTransferResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            PaymentRequest paymentRequest = new PaymentRequest(PaymentType.PERMATA_VA, customerDetails);
            Call<PermataBankTransferResponse> call = apiService.paymentBankTransferPermata(snapToken, paymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Bank Transfer Other Bank
     *
     * @param snapToken       snapToken after get payment info.
     * @param customerDetails Payment Details.
     * @param callback        Transaction callback.
     */
    public void paymentUsingBankTransferVaOther(final String snapToken,
                                                final CustomerDetailPayRequest customerDetails,
                                                final MidtransCallback<ResponseBody> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            PaymentRequest paymentRequest = new PaymentRequest(PaymentType.OTHER_VA, customerDetails);
            Call<ResponseBody> call = apiService.paymentBankTransferOther(snapToken, paymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using CIMB Clicks
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingCimbClick(final String snapToken,
                                      final MidtransCallback<CimbClicksResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            BasePaymentRequest basePaymentRequest = new BasePaymentRequest(PaymentType.CIMB_CLICKS);
            Call<CimbClicksResponse> call = apiService.paymentCimbClicks(snapToken, basePaymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using BCA Click Pay
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingBcaClickPay(final String snapToken,
                                        final MidtransCallback<BcaKlikpayResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            BasePaymentRequest basePaymentRequest = new BasePaymentRequest(PaymentType.BCA_KLIKPAY);
            Call<BcaKlikpayResponse> call = apiService.paymentBcaClickPay(snapToken, basePaymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using BRI Epay
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingBriEpay(final String snapToken,
                                    final MidtransCallback<BriEpayPaymentResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            BasePaymentRequest basePaymentRequest = new BasePaymentRequest(PaymentType.BRI_EPAY);
            Call<BriEpayPaymentResponse> call = apiService.paymentBriEpay(snapToken, basePaymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Akulaku
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingAkulaku(final String snapToken,
                                    final MidtransCallback<AkulakuResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            BasePaymentRequest basePaymentRequest = new BasePaymentRequest(PaymentType.AKULAKU);
            Call<AkulakuResponse> call = apiService.paymentAkulaku(snapToken, basePaymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Mandiri Echannel
     *
     * @param snapToken                snapToken after get payment info.
     * @param customerDetailPayRequest Payment Details.zz
     * @param callback                 Transaction callback.
     */
    public void paymentUsingMandiriEcash(final String snapToken,
                                         final CustomerDetailPayRequest customerDetailPayRequest,
                                         final MidtransCallback<MandiriEcashResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            PaymentRequest paymentRequest = new PaymentRequest(PaymentType.MANDIRI_ECASH, customerDetailPayRequest);
            Call<MandiriEcashResponse> call = apiService.paymentMandiriEcash(snapToken, paymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Gopay
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingGopay(final String snapToken,
                                  final String gopayAccountNumber,
                                  final MidtransCallback<GopayResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            GopayPaymentRequest gopayPaymentRequest = new GopayPaymentRequest(PaymentType.GOPAY, gopayAccountNumber);
            Call<GopayResponse> call = apiService.paymentUsingGoPay(snapToken, gopayPaymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Telkomsel Cash
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingTelkomselCash(final String snapToken,
                                          final String customerNumber,
                                          final MidtransCallback<TelkomselCashResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            TelkomselCashPaymentRequest telkomselCashPaymentRequest = new TelkomselCashPaymentRequest(PaymentType.TELKOMSEL_CASH, customerNumber);
            Call<TelkomselCashResponse> call = apiService.paymentUsingTelkomselCash(snapToken, telkomselCashPaymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Indomaret
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingIndomaret(final String snapToken,
                                      final MidtransCallback<IndomaretPaymentResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            BasePaymentRequest basePaymentRequest = new BasePaymentRequest(PaymentType.INDOMARET);
            Call<IndomaretPaymentResponse> call = apiService.paymentIndomaret(snapToken, basePaymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Klik Bca
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingKlikBca(final String snapToken,
                                    final String klikBcaUserId,
                                    final MidtransCallback<KlikBcaResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            KlikBcaPaymentRequest paymentRequest = new KlikBcaPaymentRequest(PaymentType.KLIK_BCA, klikBcaUserId);
            Call<KlikBcaResponse> call = apiService.paymentKlikBca(snapToken, paymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Mandiri ClickPay
     *
     * @param snapToken             snapToken after get payment info.
     * @param mandiriClickpayParams parameter for Mandiri Clickpay
     * @param callback              Transaction callback.
     */
    public void paymentUsingMandiriClickPay(final String snapToken,
                                            final MandiriClickpayParams mandiriClickpayParams,
                                            final MidtransCallback<MandiriClickpayResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            MandiriClickpayPaymentRequest paymentRequest = new MandiriClickpayPaymentRequest(PaymentType.MANDIRI_CLICKPAY, mandiriClickpayParams);
            Call<MandiriClickpayResponse> call = apiService.paymentMandiriClickpay(snapToken, paymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for card payment using snap backend.
     * <p>
     * // * @param paymentRequest Payment details.
     *
     * @param callback Transaction callback
     */

    public void paymentUsingCreditCard(final String snapToken,
                                       final CreditCardPaymentParams creditCardPaymentParams,
                                       final CustomerDetailPayRequest customerDetailPayRequest,
                                       final MidtransCallback<CreditCardResponse> callback) {

        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            CreditCardPaymentRequest creditCardPaymentRequest = new CreditCardPaymentRequest(PaymentType.CREDIT_CARD,
                    creditCardPaymentParams,
                    customerDetailPayRequest);
            Call<CreditCardResponse> call = apiService.paymentUsingCreditCard(snapToken, creditCardPaymentRequest);
            handleCall(call, callback);
        }
    }

    public void paymentUsingDanamonOnline(final String snapToken,
                                          final MidtransCallback<DanamonOnlineResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            BasePaymentRequest basePaymentRequest = new BasePaymentRequest(PaymentType.DANAMON_ONLINE);
            Call<DanamonOnlineResponse> call = apiService.paymentUsingDanamonOnline(snapToken, basePaymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * Get points of given card
     *
     * @param snapToken snap token
     * @param cardToken credit card token
     * @param callback  BNI points callback instance
     */
    public void getBanksPoint(final String snapToken,
                              final String cardToken,
                              final MidtransCallback<PointResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            Call<PointResponse> basePaymentResponseCall = apiService.getBanksPoint(snapToken, cardToken);
            handleCall(basePaymentResponseCall, callback);
        }
    }
}