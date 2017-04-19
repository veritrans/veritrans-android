package com.midtrans.sdk.ui.views.ebanking.cimb_clicks;

import com.midtrans.sdk.core.models.snap.ebanking.cimbclicks.CimbClicksPaymentResponse;

/**
 * Created by rakawm on 4/10/17.
 */

public interface CimbClicksView {
    void onCimbClicksSuccess(CimbClicksPaymentResponse paymentResponse);

    void onCimbClicksFailure(CimbClicksPaymentResponse paymentResponse);

    void onCimbClicksError(String message);
}
