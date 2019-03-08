package com.midtrans.sdk.uikit.base.contract;

public interface BaseCreditCardContract extends BaseContract {

    void onDeleteCardSuccess(String maskedCard);

    void onDeleteCardFailure();
}