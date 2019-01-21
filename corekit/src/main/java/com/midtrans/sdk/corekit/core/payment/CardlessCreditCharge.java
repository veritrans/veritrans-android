package com.midtrans.sdk.corekit.core.payment;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CardlessCreditAkulakuPaymentResponse;

public class CardlessCreditCharge extends BaseGroupPayment {

    /**
     * Start payment using bank transfer and va with Akulaku.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingAkulaku(final String snapToken,
                                           final MidtransCallback<CardlessCreditAkulakuPaymentResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingAkulaku(snapToken, callback);
        }
    }
}