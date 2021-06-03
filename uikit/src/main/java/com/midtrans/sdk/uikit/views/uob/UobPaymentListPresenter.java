package com.midtrans.sdk.uikit.views.uob;

import android.content.Context;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.uikit.PaymentMethods;
import com.midtrans.sdk.uikit.models.EnabledPayments;
import com.midtrans.sdk.uikit.models.UobPayment;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;

import java.util.ArrayList;
import java.util.List;

import static com.midtrans.sdk.corekit.core.Constants.TAG;

public class UobPaymentListPresenter {
    private final Context context;
    private final List<EnabledPayment> uobPaymentList;
    private final String BACK_BUTTON_NAME = "Back";

    public UobPaymentListPresenter(Context context, EnabledPayments enabledPayments) {
        this.context = context;
        this.uobPaymentList = new ArrayList<>();

        if (enabledPayments != null) {
            this.uobPaymentList.addAll(enabledPayments.getEnabledPayments());
        }
    }

    public List<UobPayment> getUobPaymentList() {
        List<UobPayment> payments = new ArrayList<>();
        if (uobPaymentList != null && !uobPaymentList.isEmpty()) {
            for (EnabledPayment payment : uobPaymentList) {
                UobPayment model = PaymentMethods.createUobPaymentModel(context, payment.getType(), payment.getStatus());
                if (model != null) {
                    payments.add(model);
                }
            }
        }

        SdkUIFlowUtil.sortUobMethodsByPriority(payments);

        return payments;
    }

    public void trackButtonClick(String buttonName, String pageName) {
        try {
            MidtransSDK.getInstance().getmMixpanelAnalyticsManager()
                    .trackButtonClicked(MidtransSDK.getInstance().readAuthenticationToken(), buttonName, pageName);
        } catch (NullPointerException e) {
            Logger.e(TAG, "trackButtonClick():" + e.getMessage());
        }
    }

    public void trackPageView(String pageName, boolean isFirstPage) {
        try {
            MidtransSDK.getInstance().getmMixpanelAnalyticsManager()
                    .trackPageViewed(MidtransSDK.getInstance().readAuthenticationToken(), pageName, isFirstPage);
        } catch (NullPointerException e) {
            Logger.e(TAG, "trackPageView():" + e.getMessage());
        }
    }

    public void trackBackButtonClick(String pageName) {
        trackButtonClick(BACK_BUTTON_NAME, pageName);
    }
}
