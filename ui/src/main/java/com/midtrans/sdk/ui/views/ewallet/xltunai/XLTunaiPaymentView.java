package com.midtrans.sdk.ui.views.ewallet.xltunai;

import com.midtrans.sdk.core.models.snap.ewallet.xltunai.XlTunaiPaymentResponse;

/**
 * Created by ziahaqi on 4/7/17.
 */

public interface XLTunaiPaymentView {

    void onPaymentError(String message);

    void onPaymentFailure(XlTunaiPaymentResponse response);

    void onPaymentSuccess(XlTunaiPaymentResponse response);
}
