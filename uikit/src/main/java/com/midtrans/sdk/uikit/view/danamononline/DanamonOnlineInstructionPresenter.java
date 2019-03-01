package com.midtrans.sdk.uikit.view.danamononline;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.DanamonOnlineResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.payment.OnlineDebitCharge;
import com.midtrans.sdk.uikit.base.composer.BasePaymentPresenter;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;

public class DanamonOnlineInstructionPresenter extends BasePaymentPresenter<BasePaymentContract> {

    private PaymentInfoResponse paymentInfoResponse;

    DanamonOnlineInstructionPresenter(BasePaymentContract view, PaymentInfoResponse paymentInfoResponse) {
        super();
        this.view = view;
        this.paymentInfoResponse = paymentInfoResponse;
    }

    void startCimbClicksPayment(String token) {
        OnlineDebitCharge.paymentUsingDanamonOnline(token, new MidtransCallback<DanamonOnlineResponse>() {
            @Override
            public void onSuccess(DanamonOnlineResponse data) {
                view.onPaymentSuccess(data);
            }

            @Override
            public void onFailed(Throwable throwable) {
                view.onPaymentError(throwable);
            }
        });
    }

}