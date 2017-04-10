package com.midtrans.sdk.ui.views.ewallet.tcash;

import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentResponse;

/**
 * Created by ziahaqi on 4/7/17.
 */

public interface TelkomselCashView {

    void onTelkomselCashPaymentSuccess(TelkomselCashPaymentResponse response);

    void onTelkomselCashPaymentFailure(TelkomselCashPaymentResponse response);

    void onTelkomselCashPaymentError(String message);
}
