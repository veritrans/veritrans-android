package com.midtrans.sdk.uikit.abstracts;

import com.midtrans.sdk.corekit.core.MidtransSDK;

/**
 * Created by ziahaqi on 7/28/17.
 */

public class BasePresenter<V> {
    protected String TAG = getClass().getSimpleName();

    protected V view;
    private MidtransSDK midtransSDK;

    public BasePresenter() {
        midtransSDK = MidtransSDK.getInstance();
    }

    public MidtransSDK getMidtransSDK() {
        if (midtransSDK == null) {
            MidtransSDK.getInstance();
        }
        return midtransSDK;
    }
}
