package com.midtrans.sdk.uikit.abstracts;

import com.midtrans.sdk.corekit.core.MidtransSDK;

/**
 * Created by ziahaqi on 7/28/17.
 */

public class BasePresenter<V extends BaseView> {
    protected String TAG = getClass().getSimpleName();

    protected V view;
    private volatile MidtransSDK midtransSDK;

    public BasePresenter() {
        midtransSDK = MidtransSDK.getInstance();
    }

    public MidtransSDK getMidtransSDK() {
        if (midtransSDK == null) {
            midtransSDK = MidtransSDK.getInstance();
            if (midtransSDK.isSdkNotAvailable()) {
                if (view != null) {
                    view.onNullInstanceSdk();
                }
            }
        }

        return midtransSDK;
    }

}
