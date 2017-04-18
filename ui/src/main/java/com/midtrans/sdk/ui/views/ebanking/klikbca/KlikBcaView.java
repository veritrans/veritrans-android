package com.midtrans.sdk.ui.views.ebanking.klikbca;

import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentResponse;

/**
 * Created by rakawm on 4/7/17.
 */

public interface KlikBcaView {
    void onKlikBcaSuccess(KlikBcaPaymentResponse paymentResponse);

    void onKlikBcaFailure(KlikBcaPaymentResponse paymentResponse);

    void onKlikBcaError(String message);
}
