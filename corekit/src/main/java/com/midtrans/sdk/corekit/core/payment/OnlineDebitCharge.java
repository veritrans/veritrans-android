package com.midtrans.sdk.corekit.core.payment;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaKlikpayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BriEpayPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CimbClicksResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.DanamonOnlineResponse;

public class OnlineDebitCharge extends BaseGroupPayment {
    /**
     * Start payment using bank transfer and va with CIMB Clicks.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingCimbClicks(final String snapToken,
                                              final MidtransCallback<CimbClicksResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingCimbClick(snapToken, callback);
        }
    }

    /**
     * Start payment using bank transfer and va with CIMB Clicks.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingBcaKlikpay(final String snapToken,
                                              final MidtransCallback<BcaKlikpayResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingBcaClickPay(snapToken, callback);
        }
    }

    /**
     * Start payment using bank transfer and va with Danamon Online.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingDanamonOnline(final String snapToken,
                                                 final MidtransCallback<DanamonOnlineResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingDanamonOnline(snapToken, callback);
        }
    }

    /**
     * Start payment using bank transfer and va with BRI Epay.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingBriEpay(final String snapToken,
                                           final MidtransCallback<BriEpayPaymentResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingBriEpay(snapToken, callback);
        }
    }
}