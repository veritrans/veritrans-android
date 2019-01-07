package com.midtrans.sdk.corekit.core.payment;

import android.support.annotation.NonNull;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.va.OtherPaymentResponse;

public class BankTransferCharge extends BaseGroupPayment {

    /**
     * Start payment using bank transfer and va with BCA.
     *
     * @param snapToken       token after making checkoutWithTransaction.
     * @param customerDetails for putting bank transfer request.
     * @param callback        for receiving callback from request.
     */
    public static void paymentUsingBankTransferVaBca(@NonNull final String snapToken,
                                                     @NonNull final CustomerDetailPayRequest customerDetails,
                                                     @NonNull final MidtransCallback<BasePaymentResponse> callback) {
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
    public static void paymentUsingBankTransferVaBni(@NonNull final String snapToken,
                                                     @NonNull final CustomerDetailPayRequest customerDetails,
                                                     @NonNull final MidtransCallback<BasePaymentResponse> callback) {
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
    public static void paymentUsingBankTransferVaPermata(@NonNull final String snapToken,
                                                         @NonNull final CustomerDetailPayRequest customerDetails,
                                                         @NonNull final MidtransCallback<BasePaymentResponse> callback) {
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
    public static void paymentUsingBankTransferVaOther(@NonNull final String snapToken,
                                                       @NonNull final CustomerDetailPayRequest customerDetails,
                                                       @NonNull final MidtransCallback<OtherPaymentResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingBankTransferVaOther(snapToken, customerDetails, callback);
        }
    }
}