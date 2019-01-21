package com.midtrans.sdk.corekit.core.payment;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BankTransferVaBcaPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BankTransferVaBniPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BankTransferVaOtherPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BankTransferVaPermataPaymentResponse;

public class BankTransferCharge extends BaseGroupPayment {

    /**
     * Start payment using bank transfer and va with BCA.
     *
     * @param snapToken       token after making checkoutWithTransaction.
     * @param customerDetails for putting bank transfer request.
     * @param callback        for receiving callback from request.
     */
    public static void paymentUsingBankTransferVaBca(final String snapToken,
                                                     final CustomerDetailPayRequest customerDetails,
                                                     final MidtransCallback<BankTransferVaBcaPaymentResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingBankTransferVaBca(snapToken, customerDetails, callback);
        }
    }

    /**
     * Start payment using bank transfer and va with BNI.
     *
     * @param snapToken       token after making checkoutWithTransaction.
     * @param customerDetails for putting bank transfer request.
     * @param callback        for receiving callback from request.
     */
    public static void paymentUsingBankTransferVaBni(final String snapToken,
                                                     final CustomerDetailPayRequest customerDetails,
                                                     final MidtransCallback<BankTransferVaBniPaymentResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingBankTransferVaBni(snapToken, customerDetails, callback);
        }
    }

    /**
     * Start payment using bank transfer and va with PERMATA.
     *
     * @param snapToken       token after making checkoutWithTransaction.
     * @param customerDetails for putting bank transfer request.
     * @param callback        for receiving callback from request.
     */
    public static void paymentUsingBankTransferVaPermata(final String snapToken,
                                                         final CustomerDetailPayRequest customerDetails,
                                                         final MidtransCallback<BankTransferVaPermataPaymentResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingBankTransferVaPermata(snapToken, customerDetails, callback);
        }
    }

    /**
     * Start payment using bank transfer and va with Other Bank.
     *
     * @param snapToken       token after making checkoutWithTransaction.
     * @param customerDetails for putting bank transfer request.
     * @param callback        for receiving callback from request.
     */
    public static void paymentUsingBankTransferVaOther(final String snapToken,
                                                       final CustomerDetailPayRequest customerDetails,
                                                       final MidtransCallback<BankTransferVaOtherPaymentResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingBankTransferVaOther(snapToken, customerDetails, callback);
        }
    }
}