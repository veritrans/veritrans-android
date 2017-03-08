package com.midtrans.sdk.ui;

import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by ziahaqi on 2/19/17.
 */

public interface MidtransUiCallback{

    void onFinished(PaymentResult result);
}
