package com.midtrans.sdk.corekit.core.grouppayment;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.utilities.Validation;

public class EWalletCharge extends PaymentsGroupBase {
    /**
     * Start payment using bank transfer and va with Mandiri Ecash.
     *
     * @param snapToken                token after making checkoutWithTransaction.
     * @param customerDetailPayRequest for putting bank transfer request.
     * @param callback                 for receiving callback from request.
     */
    public static void paymentUsingMandiriEcash(final String snapToken,
                                                final CustomerDetailPayRequest customerDetailPayRequest,
                                                final MidtransCallback<BasePaymentResponse> callback) {
        if (Validation.isValidForNetworkCall(getSdkContext(), callback)) {
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
                                         final MidtransCallback<BasePaymentResponse> callback) {
        if (Validation.isValidForNetworkCall(getSdkContext(), callback)) {
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
                                                 final MidtransCallback<BasePaymentResponse> callback) {
        if (Validation.isValidForNetworkCall(getSdkContext(), callback)) {
            getSnapApiManager().paymentUsingTelkomselCash(snapToken, customerNumber, callback);
        }
    }
}
