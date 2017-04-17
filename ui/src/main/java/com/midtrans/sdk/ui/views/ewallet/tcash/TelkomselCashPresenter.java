package com.midtrans.sdk.ui.views.ewallet.tcash;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentResponse;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by ziahaqi on 4/7/17.
 */

public class TelkomselCashPresenter extends BasePaymentPresenter {
    TelkomselCashView view;
    private PaymentResult<TelkomselCashPaymentResponse> paymentResult;

    public TelkomselCashPresenter(TelkomselCashView view) {
        this.view = view;
    }

    public String getMerchantLogo() {
        return midtransUi.getTransaction().merchant.preference.logoUrl;
    }

    public void startPayment(String telkomselCashToken) {
        MidtransCore.getInstance().paymentUsingTelkomselCash(midtransUi.getCheckoutToken(), telkomselCashToken,
                new MidtransCoreCallback<TelkomselCashPaymentResponse>() {
                    @Override
                    public void onSuccess(TelkomselCashPaymentResponse response) {
                        paymentResult = new PaymentResult<>(response);
                        view.onTelkomselCashPaymentSuccess(response);
                    }

                    @Override
                    public void onFailure(TelkomselCashPaymentResponse response) {
                        paymentResult = new PaymentResult<>(response);
                        view.onTelkomselCashPaymentFailure(response);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        paymentResult = new PaymentResult<>(throwable.getMessage());
                        view.onTelkomselCashPaymentError(throwable.getMessage());
                    }
                });
    }

    public boolean isShowPaymentStatus() {
        return midtransUi.getCustomSetting().isShowPaymentStatus();
    }

    public PaymentResult<TelkomselCashPaymentResponse> getPaymentResult() {
        return paymentResult;
    }
}
