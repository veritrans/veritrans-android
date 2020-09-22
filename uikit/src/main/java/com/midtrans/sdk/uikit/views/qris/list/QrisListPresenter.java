package com.midtrans.sdk.uikit.views.qris.list;

import static com.midtrans.sdk.corekit.core.Constants.TAG;

import android.content.Context;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.uikit.PaymentMethods;
import com.midtrans.sdk.uikit.models.EnabledPayments;
import com.midtrans.sdk.uikit.models.Qris;
import java.util.ArrayList;
import java.util.List;

public class QrisListPresenter {
    private final Context context;
    private final List<EnabledPayment> qrisList;
    private final String BACK_BUTTON_NAME = "Back";

    QrisListPresenter(Context context, EnabledPayments enabledPayments) {
        this.context = context;
        this.qrisList = new ArrayList<>();

        if (enabledPayments != null) {
            this.qrisList.addAll(enabledPayments.getEnabledPayments());
        }
    }

    List<Qris> getQrisList() {
        List<Qris> qrisList = new ArrayList<>();
        if (this.qrisList != null && !this.qrisList.isEmpty()) {
            for (EnabledPayment qris : this.qrisList) {
                Qris model = PaymentMethods.createQrisModel(context, qris.getType(), qris.getStatus());
                if (model != null) {
                    qrisList.add(model);
                }
            }
        }
        //SdkUIFlowUtil.sortBankTransferMethodsByPriority(banks);
        return qrisList;
    }

    public void trackButtonClick(String buttonName, String pageName) {
        try {
            MidtransSDK
                .getInstance()
                .getmMixpanelAnalyticsManager()
                .trackButtonClicked(
                    MidtransSDK
                        .getInstance()
                        .readAuthenticationToken(), buttonName, pageName
                );
        } catch (NullPointerException e) {
            Logger.e(TAG, "trackButtonClick():" + e.getMessage());
        }
    }

    public void trackPageView(String pageName, boolean isFirstPage) {
        try {
            MidtransSDK
                .getInstance()
                .getmMixpanelAnalyticsManager()
                .trackPageViewed(
                    MidtransSDK
                        .getInstance()
                        .readAuthenticationToken(), pageName, isFirstPage
                );
        } catch (NullPointerException e) {
            Logger.e(TAG, "trackPageView():" + e.getMessage());
        }
    }

    public void trackBackButtonClick(String pageName) {
        trackButtonClick(BACK_BUTTON_NAME, pageName);
    }
}
