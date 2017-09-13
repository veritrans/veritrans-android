package com.midtrans.sdk.uikit.abstracts;

import com.midtrans.sdk.corekit.core.MidtransSDK;

/**
 * Created by ziahaqi on 7/28/17.
 */

public class BasePaymentPresenter<V> extends BasePresenter<V> {

    public boolean isShowPaymentStatusPage() {
        MidtransSDK midtransSdk = MidtransSDK.getInstance();
        if (midtransSdk != null && midtransSdk.getUIKitCustomSetting() != null && midtransSdk.getUIKitCustomSetting().isShowPaymentStatus()) {
            return true;
        }
        return false;
    }
}
