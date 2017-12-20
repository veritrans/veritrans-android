package com.midtrans.sdk.uikit.abstracts;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.TransactionResponse;

/**
 * Created by ziahaqi on 7/28/17.
 */

public class BasePaymentPresenter<V extends BaseView> extends BasePresenter<V> {

    private final String BACK_BUTTON_NAME = "Back";
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

    public void trackButtonClick(String buttonName, String pageName) {
        try {
            getMidtransSDK().getmMixpanelAnalyticsManager()
                .trackButtonClicked(getMidtransSDK().readAuthenticationToken(), buttonName, pageName);
        } catch (NullPointerException e) {
            Logger.e(TAG, "trackButtonClick():" + e.getMessage());
        }
    }

    public void trackPageView(String pageName, boolean isFirstPage) {
        try {
            getMidtransSDK().getmMixpanelAnalyticsManager()
                .trackPageViewed(getMidtransSDK().readAuthenticationToken(), pageName, isFirstPage);
        } catch (NullPointerException e) {
            Logger.e(TAG, "trackPageView():" + e.getMessage());
        }
    }

    public void trackBackButtonClick(String pageName) {
        trackButtonClick(BACK_BUTTON_NAME, pageName);
    }

}
