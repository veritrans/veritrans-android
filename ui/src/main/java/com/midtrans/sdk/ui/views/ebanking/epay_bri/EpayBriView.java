package com.midtrans.sdk.ui.views.ebanking.epay_bri;

import com.midtrans.sdk.core.models.snap.ebanking.epaybri.EpayBriPaymentResponse;

/**
 * Created by rakawm on 4/10/17.
 */

public interface EpayBriView {
    void onEpayBriSuccess(EpayBriPaymentResponse paymentResponse);

    void onEpayBriFailure(String message);
}
