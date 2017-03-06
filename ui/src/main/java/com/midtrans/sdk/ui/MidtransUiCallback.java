package com.midtrans.sdk.ui;

import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by ziahaqi on 2/19/17.
 */

public interface MidtransUiCallback<T> extends MidtransCoreCallback<T>{
    void onFinished(PaymentResult result);
}
