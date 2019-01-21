package com.midtrans.sdk.corekit.core.payment;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.EwalletGopayPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.EwalletMandiriEcashPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.EwalletTelkomselCashPaymentResponse;

public class EWalletCharge extends BaseGroupPayment {
    /**
     * Start payment using bank transfer and va with Mandiri Ecash.
     *
     * @param snapToken                token after making checkoutWithTransaction.
     * @param customerDetailPayRequest for putting bank transfer request.
     * @param callback                 for receiving callback from request.
     */
    public static void paymentUsingMandiriEcash(final String snapToken,
                                                final CustomerDetailPayRequest customerDetailPayRequest,
                                                final MidtransCallback<EwalletMandiriEcashPaymentResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingMandiriEcash(snapToken, customerDetailPayRequest, callback);
        }
    }

    /**
     * Start payment using bank transfer and va with Gopay.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingGopay(final String snapToken,
                                         final String gopayAccountNumber,
                                         final MidtransCallback<EwalletGopayPaymentResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingGopay(snapToken, gopayAccountNumber, callback);
        }
    }

    /**
     * Start payment using bank transfer and va with Telkomsel Cash.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingTelkomselCash(final String snapToken,
                                                 final String customerNumber,
                                                 final MidtransCallback<EwalletTelkomselCashPaymentResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingTelkomselCash(snapToken, customerNumber, callback);
        }
    }
}