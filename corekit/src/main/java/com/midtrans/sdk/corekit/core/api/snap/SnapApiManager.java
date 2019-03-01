package com.midtrans.sdk.corekit.core.api.snap;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.base.network.BaseServiceManager;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.BasePaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.PaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.CreditCardPaymentParams;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.CreditCardPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.klikbca.KlikBcaPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.mandiriclick.MandiriClickpayParams;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.mandiriclick.MandiriClickpayPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.telkomsel.TelkomselCashPaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.AkulakuResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.AlfamartPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaBankTransferReponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaKlikpayResponse;
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
import com.midtrans.sdk.corekit.core.api.snap.model.payment.PaymentStatusResponse;
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
    public void getPaymentInfo(String snapToken,
                               MidtransCallback<PaymentInfoResponse> callback) {

        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            Call<PaymentInfoResponse> call = apiService.getTransactionOptions(snapToken);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Bank Transfer BCA
     *
     * @param snapToken snapToken after get payment info.
     * @param fullName  Fullname, this is nullable.
     * @param phone     phone, this is nullable.
     * @param email     email, this is nullable.
     * @param callback  Transaction callback.
     */
    public void paymentUsingBankTransferVaBca(String snapToken,
                                              String fullName,
                                              String phone,
                                              String email,
                                              MidtransCallback<BcaBankTransferReponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            CustomerDetailPayRequest customerDetails = new CustomerDetailPayRequest(fullName, email, phone);
            PaymentRequest paymentRequest = new PaymentRequest(PaymentType.BCA_VA, customerDetails);
            Call<BcaBankTransferReponse> call = apiService.paymentBankTransferBca(snapToken, paymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Bank Transfer BNI
     *
     * @param snapToken snapToken after get payment info.
     * @param fullName  Fullname, this is nullable.
     * @param phone     phone, this is nullable.
     * @param email     email, this is nullable.
     * @param callback  Transaction callback.
     */
    public void paymentUsingBankTransferVaBni(String snapToken,
                                              String fullName,
                                              String phone,
                                              String email,
                                              MidtransCallback<BniBankTransferResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            CustomerDetailPayRequest customerDetails = new CustomerDetailPayRequest(fullName, email, phone);
            PaymentRequest paymentRequest = new PaymentRequest(PaymentType.BNI_VA, customerDetails);
            Call<BniBankTransferResponse> call = apiService.paymentBankTransferBni(snapToken, paymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Bank Transfer Permata
     *
     * @param snapToken snapToken after get payment info.
     * @param fullName  Fullname, this is nullable.
     * @param phone     phone, this is nullable.
     * @param email     email, this is nullable.
     * @param callback  Transaction callback.
     */
    public void paymentUsingBankTransferVaPermata(String snapToken,
                                                  String fullName,
                                                  String phone,
                                                  String email,
                                                  MidtransCallback<PermataBankTransferResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            CustomerDetailPayRequest customerDetails = new CustomerDetailPayRequest(fullName, email, phone);
            PaymentRequest paymentRequest = new PaymentRequest(PaymentType.PERMATA_VA, customerDetails);
            Call<PermataBankTransferResponse> call = apiService.paymentBankTransferPermata(snapToken, paymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Bank Transfer Permata
     *
     * @param snapToken snapToken after get payment info.
     * @param fullName  Fullname, this is nullable.
     * @param phone     phone, this is nullable.
     * @param email     email, this is nullable.
     * @param callback  Transaction callback.
     */
    public void paymentUsingBankTransferVaMandiriBill(String snapToken,
                                                      String fullName,
                                                      String phone,
                                                      String email,
                                                      MidtransCallback<MandiriBillResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            CustomerDetailPayRequest customerDetails = new CustomerDetailPayRequest(fullName, email, phone);
            PaymentRequest paymentRequest = new PaymentRequest(PaymentType.ECHANNEL, customerDetails);
            Call<MandiriBillResponse> call = apiService.paymentBankTransferMandiriBill(snapToken, paymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Bank Transfer Other Bank
     *
     * @param snapToken snapToken after get payment info.
     * @param fullName  Fullname, this is nullable.
     * @param phone     phone, this is nullable.
     * @param email     email, this is nullable.
     * @param callback  Transaction callback.
     */
    public void paymentUsingBankTransferVaOther(String snapToken,
                                                String fullName,
                                                String phone,
                                                String email,
                                                MidtransCallback<ResponseBody> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            CustomerDetailPayRequest customerDetails = new CustomerDetailPayRequest(fullName, email, phone);
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
    public void paymentUsingCimbClick(String snapToken,
                                      MidtransCallback<CimbClicksResponse> callback) {
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
    public void paymentUsingBcaClickPay(String snapToken,
                                        MidtransCallback<BcaKlikpayResponse> callback) {
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
    public void paymentUsingBriEpay(String snapToken,
                                    MidtransCallback<BriEpayPaymentResponse> callback) {
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
    public void paymentUsingAkulaku(String snapToken,
                                    MidtransCallback<AkulakuResponse> callback) {
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
     * @param callback                 Transaction callback.
     */
    public void paymentUsingMandiriEcash(String snapToken,
                                         MidtransCallback<MandiriEcashResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            BasePaymentRequest paymentRequest = new BasePaymentRequest(PaymentType.MANDIRI_ECASH);
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
    public void paymentUsingGopay(String snapToken,
                                  MidtransCallback<GopayResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            BasePaymentRequest basePaymentRequest = new BasePaymentRequest(PaymentType.GOPAY);
            Call<GopayResponse> call = apiService.paymentUsingGoPay(snapToken, basePaymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Telkomsel Cash
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingTelkomselCash(String snapToken,
                                          String customerNumber,
                                          MidtransCallback<TelkomselCashResponse> callback) {
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
    public void paymentUsingIndomaret(String snapToken,
                                      MidtransCallback<IndomaretPaymentResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            BasePaymentRequest basePaymentRequest = new BasePaymentRequest(PaymentType.INDOMARET);
            Call<IndomaretPaymentResponse> call = apiService.paymentIndomaret(snapToken, basePaymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Alfamart
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingAlfamart(String snapToken,
                                      MidtransCallback<AlfamartPaymentResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            BasePaymentRequest basePaymentRequest = new BasePaymentRequest(PaymentType.ALFAMART);
            Call<AlfamartPaymentResponse> call = apiService.paymentAlfamart(snapToken, basePaymentRequest);
            handleCall(call, callback);
        }
    }

    /**
     * This method is used for Payment Using Klik Bca
     *
     * @param snapToken snapToken after get payment info.
     * @param callback  Transaction callback.
     */
    public void paymentUsingKlikBca(String snapToken,
                                    String klikBcaUserId,
                                    MidtransCallback<KlikBcaResponse> callback) {
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
    public void paymentUsingMandiriClickPay(String snapToken,
                                            MandiriClickpayParams mandiriClickpayParams,
                                            MidtransCallback<MandiriClickpayResponse> callback) {
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

    public void paymentUsingCreditCard(String snapToken,
                                       CreditCardPaymentParams creditCardPaymentParams,
                                       CustomerDetailPayRequest customerDetailPayRequest,
                                       MidtransCallback<CreditCardResponse> callback) {

        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            CreditCardPaymentRequest creditCardPaymentRequest = new CreditCardPaymentRequest(PaymentType.CREDIT_CARD,
                    creditCardPaymentParams,
                    customerDetailPayRequest);
            Call<CreditCardResponse> call = apiService.paymentUsingCreditCard(snapToken, creditCardPaymentRequest);
            handleCall(call, callback);
        }
    }

    public void paymentUsingDanamonOnline(String snapToken,
                                          MidtransCallback<DanamonOnlineResponse> callback) {
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
    public void getBanksPoint(String snapToken,
                              String cardToken,
                              MidtransCallback<PointResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            Call<PointResponse> basePaymentResponseCall = apiService.getBanksPoint(snapToken, cardToken);
            handleCall(basePaymentResponseCall, callback);
        }
    }

    /**
     * Get payment status
     *
     * @param snapToken snap token
     */
    public void getPaymentStatus(String snapToken,
                              MidtransCallback<PaymentStatusResponse> callback) {
        if (isSnapTokenAvailable(callback, snapToken, apiService)) {
            Call<PaymentStatusResponse> basePaymentResponseCall = apiService.getPaymentStatus(snapToken);
            handleCall(basePaymentResponseCall, callback);
        }
    }
}