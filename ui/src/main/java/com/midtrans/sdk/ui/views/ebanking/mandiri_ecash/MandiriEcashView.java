package com.midtrans.sdk.ui.views.ebanking.mandiri_ecash;

import com.midtrans.sdk.core.models.snap.ebanking.mandiriecash.MandiriECashPaymentResponse;

/**
 * Created by rakawm on 4/10/17.
 */

public interface MandiriEcashView {
    void onMandiriEcashSuccess(MandiriECashPaymentResponse response);

    void onMandiriEcashFailure(String message);
}
