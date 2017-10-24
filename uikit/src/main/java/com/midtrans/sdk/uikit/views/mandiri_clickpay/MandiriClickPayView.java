package com.midtrans.sdk.uikit.views.mandiri_clickpay;

import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;

/**
 * Created by ziahaqi on 10/16/17.
 */

public interface MandiriClickPayView extends BasePaymentView {

    void onGetCardTokenSuccess(TokenDetailsResponse response);

    void onGetCardTokenFailure(TokenDetailsResponse response);

    void onGetCardTokenError(Throwable error);
}
