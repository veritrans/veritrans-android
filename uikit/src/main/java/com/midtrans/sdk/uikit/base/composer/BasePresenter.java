package com.midtrans.sdk.uikit.base.composer;

import com.midtrans.sdk.corekit.MidtransSdk;
import com.midtrans.sdk.uikit.MidtransKit;
import com.midtrans.sdk.uikit.base.contract.BaseContract;

public class BasePresenter<V extends BaseContract> {
    protected String TAG = getClass().getSimpleName();

    protected V view;
    private volatile MidtransKit midtransKit;
    private volatile MidtransSdk midtransSDK;

    public BasePresenter() {
        midtransSDK = MidtransSdk.getInstance();
        midtransKit = MidtransKit.getInstance();
    }

    public MidtransSdk getMidtransSdk() {
        if (midtransSDK == null) {
            midtransSDK = MidtransSdk.getInstance();
            if (midtransSDK == null) {
                if (view != null) {
                    view.onNullInstanceSdk();
                }
            }
        }

        return midtransSDK;
    }

    public MidtransKit getMidtransKit() {
        if (midtransKit == null) {
            midtransKit = MidtransKit.getInstance();
            if (midtransKit == null) {
                if (view != null) {
                    view.onNullInstanceSdk();
                }
            }
        }

        return midtransKit;
    }

}