package com.midtrans.sdk.corekit.core.payment;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.mandiriclick.MandiriClickpayParams;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.DirectDebitKlikBcaResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.DirectDebitMandiriClickpayResponse;

public class DirectDebitCharge extends BaseGroupPayment {

    /**
     * Start payment using bank transfer and va with Klik BCA.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingKlikBca(final String snapToken,
                                           final String klikBcaUserId,
                                           final MidtransCallback<DirectDebitKlikBcaResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingKlikBca(snapToken, klikBcaUserId, callback);
        }
    }


    /**
     * Start payment using bank transfer and va with Mandiri Ecash.
     *
     * @param snapToken             token after making checkoutWithTransaction.
     * @param mandiriClickpayParams for putting bank transfer request.
     * @param callback              for receiving callback from request.
     */
    public static void paymentUsingMandiriClickPay(final String snapToken,
                                                   final MandiriClickpayParams mandiriClickpayParams,
                                                   final MidtransCallback<DirectDebitMandiriClickpayResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingMandiriClickPay(snapToken, mandiriClickpayParams, callback);
        }
    }

}