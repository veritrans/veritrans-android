package com.midtrans.sdk.uikit.views.shopeepay.payment;

import android.app.Activity;
import android.content.Context;
import com.midtrans.sdk.uikit.BuildConfig;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.Utils;

class ShopeePayPaymentPresenter extends BasePaymentPresenter<ShopeePayPaymentView> {

    private Boolean isTablet, isShopeeInstalled;
    static final String SHOPEE_PACKAGE_NAME = "com.shopee.id";

    ShopeePayPaymentPresenter(ShopeePayPaymentView view) {
        super();
        this.view = view;
    }

    void setTabletDevice(Activity activity) {
        isTablet = SdkUIFlowUtil.getDeviceType(activity).equals(SdkUIFlowUtil.TYPE_TABLET) && SdkUIFlowUtil.isDeviceTablet(activity);
    }

    void setShopeeInstalled(Context context) {
        if (BuildConfig.FLAVOR.equals(UiKitConstants.ENVIRONMENT_PRODUCTION)){
            isShopeeInstalled = Utils.isAppInstalled(context, SHOPEE_PACKAGE_NAME);
        }
        isShopeeInstalled = false;
    }

    void openShopeeInPlayStore(Context context) {
        Utils.openAppInPlayStore(context, SHOPEE_PACKAGE_NAME);
    }

    Boolean getTabletDevice() {
        return isTablet;
    }

    Boolean getShopeeInstalled() {
        return isShopeeInstalled;
    }

    void startShopeePayPayment() {
        //TODO
    }

    void getPaymentStatus() {
        //TODO
    }
}
