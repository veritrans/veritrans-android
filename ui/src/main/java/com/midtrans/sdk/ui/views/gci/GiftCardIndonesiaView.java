package com.midtrans.sdk.ui.views.gci;

import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentResponse;

/**
 * Created by rakawm on 4/6/17.
 */

public interface GiftCardIndonesiaView {
    void onGiftCardIndonesiaSuccess(GiftCardPaymentResponse response);

    void onGiftCardIndonesiaFailure(GiftCardPaymentResponse response);

    void onGiftCardIndonesiaError(String message);
}
