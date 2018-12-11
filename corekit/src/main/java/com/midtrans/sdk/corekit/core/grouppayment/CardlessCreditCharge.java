package com.midtrans.sdk.corekit.core.grouppayment;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.utilities.Validation;

public class CardlessCreditCharge extends PaymentsGroupBase {

    /**
     * Start payment using bank transfer and va with Akulaku.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public void paymentUsingAkulaku(final String snapToken,
                                           final MidtransCallback<BasePaymentResponse> callback) {
        if (isValidForNetworkCall(getSdkContext(), callback)) {
            getSnapApiManager().paymentUsingAkulaku(snapToken, callback);
        }
    }
}
