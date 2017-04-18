package com.midtrans.sdk.ui.views.cstore.indomaret;

import com.midtrans.sdk.core.models.snap.conveniencestore.indomaret.IndomaretPaymentResponse;

/**
 * Created by rakawm on 4/6/17.
 */

public interface IndomaretView {
    void onIndomaretPaymentSuccess(IndomaretPaymentResponse response);

    void onIndomaretPaymentFailure(IndomaretPaymentResponse response);

    void onIndomaretPaymentError(String message);
}
