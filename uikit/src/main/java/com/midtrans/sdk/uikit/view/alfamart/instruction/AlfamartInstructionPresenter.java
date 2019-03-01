package com.midtrans.sdk.uikit.view.alfamart.instruction;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.AlfamartPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.payment.ConvenienceStoreCharge;
import com.midtrans.sdk.uikit.base.composer.BasePaymentPresenter;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;

public class AlfamartInstructionPresenter extends BasePaymentPresenter<BasePaymentContract> {

    private PaymentInfoResponse paymentInfoResponse;

    AlfamartInstructionPresenter(BasePaymentContract view, PaymentInfoResponse paymentInfoResponse) {
        super();
        this.view = view;
        this.paymentInfoResponse = paymentInfoResponse;
    }

    void startAlfamartPayment(String token) {
        ConvenienceStoreCharge.paymentUsingAlfamart(token, new MidtransCallback<AlfamartPaymentResponse>() {
            @Override
            public void onSuccess(AlfamartPaymentResponse data) {
                view.onPaymentSuccess(data);
            }

            @Override
            public void onFailed(Throwable throwable) {
                view.onPaymentError(throwable);
            }
        });
    }

}