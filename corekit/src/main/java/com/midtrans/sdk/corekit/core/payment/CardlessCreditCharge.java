package com.midtrans.sdk.corekit.core.payment;

import android.support.annotation.NonNull;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BasePaymentResponse;

public class CardlessCreditCharge extends BaseGroupPayment {

    /**
     * Start payment using bank transfer and va with Akulaku.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingAkulaku(@NonNull final String snapToken,
                                           @NonNull final MidtransCallback<BasePaymentResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingAkulaku(snapToken, callback);
        }
    }
}