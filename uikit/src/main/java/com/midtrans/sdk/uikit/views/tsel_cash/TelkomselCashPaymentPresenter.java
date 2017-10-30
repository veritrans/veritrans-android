package com.midtrans.sdk.uikit.views.tsel_cash;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;

/**
 * Created by Fajar on 30/10/17.
 */

public class TelkomselCashPaymentPresenter extends BasePaymentPresenter<BasePaymentView> {
    public TelkomselCashPaymentPresenter(BasePaymentView view) {
        super();
        this.view = view;
    }

    public void startPayment(String token) {
        getMidtransSDK().paymentUsingTelkomselEcash(getMidtransSDK().readAuthenticationToken(), token,
            new TransactionCallback() {
                @Override
                public void onSuccess(TransactionResponse response) {
                    transactionResponse = response;
                    view.onPaymentSuccess(response);
                    trackEvent(AnalyticsEventName.PAGE_STATUS_PENDING);
                }

                @Override
                public void onFailure(TransactionResponse response, String reason) {
                    transactionResponse = response;
                    view.onPaymentFailure(response);
                    trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
                }

                @Override
                public void onError(Throwable error) {
                    view.onPaymentError(error);
                }
            });

    }
}
