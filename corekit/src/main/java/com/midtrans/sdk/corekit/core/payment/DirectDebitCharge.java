package com.midtrans.sdk.corekit.core.payment;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.KlikBcaResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriClickpayResponse;

public class DirectDebitCharge extends BaseGroupPayment {

    /**
     * Start payment using bank transfer and va with Klik BCA.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingKlikBca(final String snapToken,
                                           final String klikBcaUserId,
                                           final MidtransCallback<KlikBcaResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingKlikBca(snapToken, klikBcaUserId, callback);
        }
    }


    /**
     * Start payment using bank transfer and va with Mandiri Ecash.
     *
     * @param snapToken      token after making checkoutWithTransaction.
     * @param tokenId        generated tokenId
     * @param challengeToken token from mandiri token
     * @param input3         input3 from mandiri token
     * @param callback       for receiving callback from request.
     */
    public static void paymentUsingMandiriClickPay(final String snapToken,
                                                   final String tokenId,
                                                   final String challengeToken,
                                                   final String input3,
                                                   final MidtransCallback<MandiriClickpayResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingMandiriClickPay(snapToken, tokenId, challengeToken, input3, callback);
        }
    }

}