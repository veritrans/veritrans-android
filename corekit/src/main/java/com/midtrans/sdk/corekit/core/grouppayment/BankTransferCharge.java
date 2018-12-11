package com.midtrans.sdk.corekit.core.grouppayment;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.OtherPaymentResponse;

public class BankTransferCharge extends PaymentsGroupBase {

    /**
     * Start payment using bank transfer and va with BCA.
     *
     * @param snapToken       token after making checkoutWithTransaction.
     * @param customerDetails for putting bank transfer request.
     * @param callback        for receiving callback from request.
     */
    public void paymentUsingBankTransferVaBca(final String snapToken,
                                              final CustomerDetailPayRequest customerDetails,
                                              final MidtransCallback<BasePaymentResponse> callback) {
        if (isValidForNetworkCall(getSdkContext(), callback)) {
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
    public void paymentUsingBankTransferVaBni(final String snapToken,
                                              final CustomerDetailPayRequest customerDetails,
                                              final MidtransCallback<BasePaymentResponse> callback) {
        if (isValidForNetworkCall(getSdkContext(), callback)) {
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
    public void paymentUsingBankTransferVaPermata(final String snapToken,
                                                  final CustomerDetailPayRequest customerDetails,
                                                  final MidtransCallback<BasePaymentResponse> callback) {
        if (isValidForNetworkCall(getSdkContext(), callback)) {
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
    public void paymentUsingBankTransferVaOther(final String snapToken,
                                                final CustomerDetailPayRequest customerDetails,
                                                final MidtransCallback<OtherPaymentResponse> callback) {
        if (isValidForNetworkCall(getSdkContext(), callback)) {
            getSnapApiManager().paymentUsingBankTransferVaOther(snapToken, customerDetails, callback);
        }
    }
}
