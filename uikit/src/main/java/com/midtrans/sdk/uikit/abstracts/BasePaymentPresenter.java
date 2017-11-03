package com.midtrans.sdk.uikit.abstracts;

import com.midtrans.sdk.corekit.core.Logger;
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
     * tracking sdk events
     *
     * @param eventName
     */
    public void trackEvent(String eventName) {
        try {
            getMidtransSDK().getmMixpanelAnalyticsManager()
                    .trackMixpanel(getMidtransSDK().readAuthenticationToken(), eventName);
        } catch (NullPointerException e) {
            Logger.e(TAG, "trackEvent():" + e.getMessage());
        }
    }

    /**
     * tracking sdk events
     *
     * @param eventName
     * @param cardPaymentMode
     */
    public void trackEvent(String eventName, String cardPaymentMode) {
        try {
            getMidtransSDK().getmMixpanelAnalyticsManager().trackMixpanel(getMidtransSDK().readAuthenticationToken(), eventName, cardPaymentMode);
        } catch (NullPointerException e) {
            Logger.e(TAG, "trackEvent():" + e.getMessage());
        }
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
