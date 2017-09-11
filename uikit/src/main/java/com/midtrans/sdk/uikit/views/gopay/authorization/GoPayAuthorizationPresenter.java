package com.midtrans.sdk.uikit.views.gopay.authorization;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;

/**
 * Created by ziahaqi on 9/11/17.
 */

public class GoPayAuthorizationPresenter extends BasePaymentPresenter<GoPayAuthorizationView> {


    public GoPayAuthorizationPresenter(GoPayAuthorizationView view) {
        this.view = view;
    }

    public void authorizePayment(String verificationCode) {
        // todo auth gopay
    }
}
