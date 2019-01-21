package com.midtrans.sdk.corekit.core.payment;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.ConvenienceStoreIndomaretPaymentResponse;

public class ConvenienceStoreCharge extends BaseGroupPayment {

    /**
     * Start payment using bank transfer and va with Indomaret.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingIndomaret(final String snapToken,
                                             final MidtransCallback<ConvenienceStoreIndomaretPaymentResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingIndomaret(snapToken, callback);
        }
    }
}