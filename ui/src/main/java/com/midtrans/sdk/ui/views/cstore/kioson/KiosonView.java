package com.midtrans.sdk.ui.views.cstore.kioson;

import com.midtrans.sdk.core.models.snap.conveniencestore.kioson.KiosonPaymentResponse;

/**
 * Created by rakawm on 4/6/17.
 */

public interface KiosonView {
    void onKiosonPaymentSuccess(KiosonPaymentResponse response);

    void onKiosonPaymentFailure(String message);
}
