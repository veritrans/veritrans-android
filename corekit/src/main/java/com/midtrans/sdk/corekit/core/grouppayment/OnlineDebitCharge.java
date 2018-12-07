package com.midtrans.sdk.corekit.core.grouppayment;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.utilities.Validation;

public class OnlineDebitCharge extends PaymentsGroupBase {
    /**
     * Start payment using bank transfer and va with CIMB Clicks.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingCimbClicks(final String snapToken,
                                              final MidtransCallback<BasePaymentResponse> callback) {
        if (Validation.isValidForNetworkCall(getSdkContext(), callback)) {
            getSnapApiManager().paymentUsingCimbClick(snapToken, callback);
        }
    }

    /**
     * Start payment using bank transfer and va with CIMB Clicks.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingBcaClickPay(final String snapToken,
                                               final MidtransCallback<BasePaymentResponse> callback) {
        if (Validation.isValidForNetworkCall(getSdkContext(), callback)) {
            getSnapApiManager().paymentUsingBcaClickPay(snapToken, callback);
        }
    }

    /**
     * Start payment using bank transfer and va with BRI Epay.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingBriEpay(final String snapToken,
                                           final MidtransCallback<BasePaymentResponse> callback) {
        if (Validation.isValidForNetworkCall(getSdkContext(), callback)) {
            getSnapApiManager().paymentUsingBriEpay(snapToken, callback);
        }
    }
}
