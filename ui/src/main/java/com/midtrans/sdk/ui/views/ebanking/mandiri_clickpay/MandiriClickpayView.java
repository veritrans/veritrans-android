package com.midtrans.sdk.ui.views.ebanking.mandiri_clickpay;

import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentResponse;

/**
 * Created by rakawm on 4/10/17.
 */

public interface MandiriClickpayView {
    void onMandiriClickpaySuccess(MandiriClickpayPaymentResponse response);

    void onMandiriClickpayFailure(String message);
}
