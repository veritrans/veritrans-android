package com.midtrans.sdk.uikit.views.shopeepay.payment;

import android.app.Activity;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;

public class ShopeePayPaymentPresenter extends BasePaymentPresenter<ShopeePayPaymentView> {

    Boolean checkTabletDevice(Activity activity) {
        return SdkUIFlowUtil.getDeviceType(activity).equals(SdkUIFlowUtil.TYPE_TABLET) && SdkUIFlowUtil.isDeviceTablet(activity);
    }
}
