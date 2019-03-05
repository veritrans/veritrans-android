package com.midtrans.sdk.uikit.view.bcaklikpay;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaKlikPayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.payment.OnlineDebitCharge;
import com.midtrans.sdk.uikit.base.composer.BasePaymentPresenter;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;

public class BcaKlikpayInstructionPresenter extends BasePaymentPresenter<BasePaymentContract> {

    private PaymentInfoResponse paymentInfoResponse;

    BcaKlikpayInstructionPresenter(BasePaymentContract view, PaymentInfoResponse paymentInfoResponse) {
        super();
        this.view = view;
        this.paymentInfoResponse = paymentInfoResponse;
    }

    void startBcaKlikpayPayment(String token) {
        OnlineDebitCharge.paymentUsingBcaKlikpay(token, new MidtransCallback<BcaKlikPayResponse>() {
            @Override
            public void onSuccess(BcaKlikPayResponse data) {
                view.onPaymentSuccess(data);
            }

            @Override
            public void onFailed(Throwable throwable) {
                view.onPaymentError(throwable);
            }
        });
    }

}