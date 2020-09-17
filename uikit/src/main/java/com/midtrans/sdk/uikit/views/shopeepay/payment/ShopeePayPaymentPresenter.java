package com.midtrans.sdk.uikit.views.shopeepay.payment;

import android.app.Activity;
import android.content.Context;
import com.midtrans.sdk.uikit.BuildConfig;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.Utils;

class ShopeePayPaymentPresenter extends BasePaymentPresenter<ShopeePayPaymentView> {

    static final String SHOPEE_PACKAGE_NAME = "com.shopee.id";

    Boolean checkTabletDevice(Activity activity) {
        return SdkUIFlowUtil.getDeviceType(activity).equals(SdkUIFlowUtil.TYPE_TABLET) && SdkUIFlowUtil.isDeviceTablet(activity);
    }

    Boolean checkShopeeInstalled(Context context) {
        if (BuildConfig.FLAVOR.equals(UiKitConstants.ENVIRONMENT_PRODUCTION)){
            return Utils.isAppInstalled(context, SHOPEE_PACKAGE_NAME);
        }
        return false;
    }
}
