package com.midtrans.sdk.ui.views.banktransfer.payment;

import com.midtrans.sdk.ui.abtracts.BaseView;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by ziahaqi on 4/3/17.
 */

public interface BankTransferPaymentContract {

    interface BankTransferPaymentView extends BaseView<Presenter> {

        void onPaymentError(String error);

        void onPaymentFailure(PaymentResult paymentResult);

        void onPaymentSuccess(PaymentResult paymentResult);
    }


    interface Presenter {

        void trackEvent(String eventName);
    }
}
