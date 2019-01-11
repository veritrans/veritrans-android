package com.midtrans.sdk.corekit.core.payment;

import android.support.annotation.NonNull;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.GopayPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriEcashPaymentResponse;

public class EWalletCharge extends BaseGroupPayment {
    /**
     * Start payment using bank transfer and va with Mandiri Ecash.
     *
     * @param snapToken                token after making checkoutWithTransaction.
     * @param customerDetailPayRequest for putting bank transfer request.
     * @param callback                 for receiving callback from request.
     */
    public static void paymentUsingMandiriEcash(@NonNull final String snapToken,
                                                @NonNull final CustomerDetailPayRequest customerDetailPayRequest,
                                                @NonNull final MidtransCallback<MandiriEcashPaymentResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingMandiriEcash(snapToken, customerDetailPayRequest, callback);
        }
    }

    /**
     * Start payment using bank transfer and va with Gopay.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingGopay(@NonNull final String snapToken,
                                         @NonNull final String gopayAccountNumber,
                                         @NonNull final MidtransCallback<GopayPaymentResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingGopay(snapToken, gopayAccountNumber, callback);
        }
    }

    /**
     * Start payment using bank transfer and va with Telkomsel Cash.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingTelkomselCash(@NonNull final String snapToken,
                                                 @NonNull final String customerNumber,
                                                 @NonNull final MidtransCallback<BasePaymentResponse> callback) {
        if (isValidForNetworkCall(callback)) {
            getSnapApiManager().paymentUsingTelkomselCash(snapToken, customerNumber, callback);
        }
    }
}