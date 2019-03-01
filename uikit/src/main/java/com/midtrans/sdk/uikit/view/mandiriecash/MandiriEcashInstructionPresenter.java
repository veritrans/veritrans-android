package com.midtrans.sdk.uikit.view.mandiriecash;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriEcashResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.payment.EWalletCharge;
import com.midtrans.sdk.uikit.base.composer.BasePaymentPresenter;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;

public class MandiriEcashInstructionPresenter extends BasePaymentPresenter<BasePaymentContract> {

    private PaymentInfoResponse paymentInfoResponse;

    MandiriEcashInstructionPresenter(BasePaymentContract view, PaymentInfoResponse paymentInfoResponse) {
        super();
        this.view = view;
        this.paymentInfoResponse = paymentInfoResponse;
    }

    void startMandiriEcashPayment(String token) {
        EWalletCharge.paymentUsingMandiriEcash(token, new MidtransCallback<MandiriEcashResponse>() {
            @Override
            public void onSuccess(MandiriEcashResponse data) {
                view.onPaymentSuccess(data);
            }

            @Override
            public void onFailed(Throwable throwable) {
                view.onPaymentError(throwable);
            }
        });
    }

}