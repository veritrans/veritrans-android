package com.midtrans.sdk.ui.views.banktransfer.payment;

import com.midtrans.sdk.ui.abtracts.BaseView;

/**
 * Created by ziahaqi on 4/3/17.
 */

public interface BankTransferPaymentContract {

    interface bankTransferPaymentView extends BaseView<Presenter> {

    }

    interface bankTransferStatusView {

    }

    interface Presenter {

        void trackEvent(String eventName);
    }
}
