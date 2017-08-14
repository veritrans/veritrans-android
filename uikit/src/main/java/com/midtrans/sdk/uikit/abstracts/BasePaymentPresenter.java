package com.midtrans.sdk.uikit.abstracts;

import com.midtrans.sdk.corekit.core.MidtransSDK;

/**
 * Created by ziahaqi on 7/28/17.
 */

public class BasePaymentPresenter<V> extends BasePresenter<V> {

    public boolean isShowPaymentStatusPage() {
        return MidtransSDK.getInstance().getUIKitCustomSetting().isShowPaymentStatus();
    }
}
