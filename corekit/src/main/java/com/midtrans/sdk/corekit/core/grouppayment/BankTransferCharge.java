package com.midtrans.sdk.corekit.core.grouppayment;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.BcaPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.BniPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.OtherPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.PermataPaymentResponse;
import com.midtrans.sdk.corekit.utilities.Validation;

public class BankTransferCharge extends BaseGroupPayments {

    /**
     * Start payment using bank transfer and va with BCA.
     *
     * @param snapToken       token after making checkoutWithTransaction.
     * @param customerDetails for putting bank transfer request.
     * @param callback        for receiving callback from request.
     */
    public static void paymentUsingBankTransferVaBca(final String snapToken,
                                                     final CustomerDetailPayRequest customerDetails,
                                                     final MidtransCallback<BcaPaymentResponse> callback) {
        if (Validation.isValidForNetworkCall(getSdkContext(), callback)) {
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
                                                     final MidtransCallback<BniPaymentResponse> callback) {
        if (Validation.isValidForNetworkCall(getSdkContext(), callback)) {
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
                                                         final MidtransCallback<PermataPaymentResponse> callback) {
        if (Validation.isValidForNetworkCall(getSdkContext(), callback)) {
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
                                                       final MidtransCallback<OtherPaymentResponse> callback) {
        if (Validation.isValidForNetworkCall(getSdkContext(), callback)) {
            getSnapApiManager().paymentUsingBankTransferVaOther(snapToken, customerDetails, callback);
        }
    }
}
