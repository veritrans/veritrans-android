package com.midtrans.sdk.uikit.abstracts;

import com.midtrans.sdk.corekit.models.TransactionResponse;

/**
 * Created by ziahaqi on 7/28/17.
 */

public class BasePaymentPresenter<V extends BaseView> extends BasePresenter<V> {

    protected TransactionResponse transactionResponse;

    public BasePaymentPresenter() {
        super();
    }

    /**
     * check for showing status page
     *
     * @return boolean
     */
    public boolean isShowPaymentStatusPage() {
        return getMidtransSDK() != null && getMidtransSDK().getUIKitCustomSetting() != null
            && getMidtransSDK().getUIKitCustomSetting().isShowPaymentStatus();

    }

    public TransactionResponse getTransactionResponse() {
        return transactionResponse;
    }

}
