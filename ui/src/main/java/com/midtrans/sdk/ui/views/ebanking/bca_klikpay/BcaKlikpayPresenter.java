package com.midtrans.sdk.ui.views.ebanking.bca_klikpay;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.ebanking.bcaklikpay.BcaKlikpayPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by rakawm on 4/10/17.
 */

public class BcaKlikpayPresenter extends BasePaymentPresenter {
    private final BcaKlikpayView view;
    private BcaKlikpayPaymentResponse paymentResponse;

    public BcaKlikpayPresenter(BcaKlikpayView view) {
        this.view = view;
    }

    public void startPayment() {
        MidtransCore.getInstance().paymentUsingBcaKlikpay(MidtransUi.getInstance().getTransaction().token, new MidtransCoreCallback<BcaKlikpayPaymentResponse>() {
            @Override
            public void onSuccess(BcaKlikpayPaymentResponse object) {
                paymentResponse = object;
                view.onBcaKlikpaySuccess(object);
            }

            @Override
            public void onFailure(BcaKlikpayPaymentResponse object) {
                view.onBcaKlikpayFailure("Failed to pay using BCA KlikPay");
            }

            @Override
            public void onError(Throwable throwable) {
                view.onBcaKlikpayFailure(throwable.getMessage());
            }
        });
    }

    public PaymentResult<BcaKlikpayPaymentResponse> getPaymentResult() {
        return new PaymentResult<>(paymentResponse);
    }
}
