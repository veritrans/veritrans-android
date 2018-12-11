package com.midtrans.sdk.corekit.core.grouppayment;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.mandiriclick.MandiriClickpayParams;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.utilities.Constants;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.corekit.utilities.Validation;

import static com.midtrans.sdk.corekit.utilities.Constants.TAG;

public class DirectDebitCharge extends PaymentsGroupBase {

    /**
     * Start payment using bank transfer and va with Klik BCA.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public void paymentUsingKlikBca(final String snapToken,
                                           final String klikBcaUserId,
                                           final MidtransCallback<BasePaymentResponse> callback) {
        if (isValidForNetworkCall(getSdkContext(), callback)) {
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
    public void paymentUsingMandiriClickPay(final String snapToken,
                                            final MandiriClickpayParams mandiriClickpayParams,
                                            final MidtransCallback<BasePaymentResponse> callback) {
        if (isValidForNetworkCall(getSdkContext(), callback)) {
            getSnapApiManager().paymentUsingMandiriClickPay(snapToken, mandiriClickpayParams, callback);
        }
    }

}
