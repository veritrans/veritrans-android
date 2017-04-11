package com.midtrans.sdk.ui.views.ewallet.indosatdompetku;

import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentResponse;

/**
 * Created by ziahaqi on 4/11/17.
 */

public interface IndosatDompetkuView {

    void onPaymentError(String message);

    void onPaymentFailure(IndosatDompetkuPaymentResponse response);

    void onPaymentSuccess(IndosatDompetkuPaymentResponse response);
}
