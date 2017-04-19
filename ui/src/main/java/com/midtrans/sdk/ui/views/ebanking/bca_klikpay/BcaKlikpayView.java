package com.midtrans.sdk.ui.views.ebanking.bca_klikpay;

import com.midtrans.sdk.core.models.snap.ebanking.bcaklikpay.BcaKlikpayPaymentResponse;

/**
 * Created by rakawm on 4/10/17.
 */

public interface BcaKlikpayView {
    void onBcaKlikpaySuccess(BcaKlikpayPaymentResponse paymentResponse);

    void onBcaKlikpayFailure(BcaKlikpayPaymentResponse paymentResponse);

    void onBcaKlikpayError(String message);
}
