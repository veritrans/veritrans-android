package com.midtrans.sdk.uikit.view.method.mandiriclickpay;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.midtrans.model.cardtoken.CardTokenRequest;
import com.midtrans.sdk.corekit.core.api.midtrans.model.tokendetails.TokenDetailsResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriClickpayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.payment.CreditCardCharge;
import com.midtrans.sdk.corekit.core.payment.DirectDebitCharge;
import com.midtrans.sdk.uikit.MidtransKit;
import com.midtrans.sdk.uikit.base.composer.BasePaymentPresenter;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;

public class MandiriClickpayInstructionPresenter extends BasePaymentPresenter<BasePaymentContract> {

    private PaymentInfoResponse paymentInfoResponse;

    MandiriClickpayInstructionPresenter(BasePaymentContract view, PaymentInfoResponse paymentInfoResponse) {
        super();
        this.view = view;
        this.paymentInfoResponse = paymentInfoResponse;
    }

    void startMandiriClickpayPayment(String token, String cardNumber, String challengeToken, String input3) {
        DirectDebitCharge.paymentUsingMandiriClickPay(token, cardNumber, challengeToken, input3, new MidtransCallback<MandiriClickpayResponse>() {
            @Override
            public void onSuccess(MandiriClickpayResponse data) {
                view.onPaymentSuccess(data);
            }

            @Override
            public void onFailed(Throwable throwable) {
                view.onPaymentError(throwable);
            }
        });
    }

}