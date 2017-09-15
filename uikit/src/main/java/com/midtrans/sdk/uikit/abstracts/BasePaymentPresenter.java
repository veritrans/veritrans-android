package com.midtrans.sdk.uikit.abstracts;

import com.midtrans.sdk.analytics.MixpanelAnalyticsManager;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.TransactionResponse;

/**
 * Created by ziahaqi on 7/28/17.
 */

public class BasePaymentPresenter<V extends BaseView> extends BasePresenter<V> {

    private volatile MixpanelAnalyticsManager mixpanelAnalyticsManager;
    protected TransactionResponse transactionResponse;

    public BasePaymentPresenter() {
        super();
        mixpanelAnalyticsManager = getMidtransSDK().getmMixpanelAnalyticsManager();
    }

    private MixpanelAnalyticsManager getMixpanelManager() {
        if (mixpanelAnalyticsManager == null) {
            mixpanelAnalyticsManager = getMidtransSDK().getmMixpanelAnalyticsManager();
        }
        return mixpanelAnalyticsManager;
    }

    /**
     * tracking sdk events
     *
     * @param eventName
     */
    public void trackEvent(String eventName) {
        try {
            getMixpanelManager().trackMixpanel(getMidtransSDK().readAuthenticationToken(), eventName);
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
            getMixpanelManager().trackMixpanel(getMidtransSDK().readAuthenticationToken(), eventName, cardPaymentMode);
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
        if (getMidtransSDK() != null && getMidtransSDK().getUIKitCustomSetting() != null
                && getMidtransSDK().getUIKitCustomSetting().isShowPaymentStatus()) {
            return true;
        }

        return false;
    }

    public TransactionResponse getTransactionResponse() {
        return transactionResponse;
    }

}
