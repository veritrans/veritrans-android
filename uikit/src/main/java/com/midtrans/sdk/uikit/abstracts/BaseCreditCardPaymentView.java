package com.midtrans.sdk.uikit.abstracts;

/**
 * Created by ziahaqi on 8/9/17.
 */

public interface BaseCreditCardPaymentView extends BaseView {

    void onDeleteCardSuccess(String maskedCard);

    void onDeleteCardFailure();
}
