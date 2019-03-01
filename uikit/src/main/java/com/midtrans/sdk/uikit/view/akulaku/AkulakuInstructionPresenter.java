package com.midtrans.sdk.uikit.view.akulaku;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.AkulakuResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CimbClicksResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.payment.CardlessCreditCharge;
import com.midtrans.sdk.corekit.core.payment.OnlineDebitCharge;
import com.midtrans.sdk.uikit.base.composer.BasePaymentPresenter;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;

public class AkulakuInstructionPresenter extends BasePaymentPresenter<BasePaymentContract> {

    private PaymentInfoResponse paymentInfoResponse;

    AkulakuInstructionPresenter(BasePaymentContract view, PaymentInfoResponse paymentInfoResponse) {
        super();
        this.view = view;
        this.paymentInfoResponse = paymentInfoResponse;
    }

    void startAkulakuPayment(String token) {
        CardlessCreditCharge.paymentUsingAkulaku(token, new MidtransCallback<AkulakuResponse>() {
            @Override
            public void onSuccess(AkulakuResponse data) {
                view.onPaymentSuccess(data);
            }

            @Override
            public void onFailed(Throwable throwable) {
                view.onPaymentError(throwable);
            }
        });
    }

}