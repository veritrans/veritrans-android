package com.midtrans.sdk.uikit.view.method.creditcard.details;

import com.midtrans.sdk.corekit.core.api.midtrans.model.tokendetails.TokenDetailsResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.point.PointResponse;
import com.midtrans.sdk.uikit.base.contract.BaseCreditCardContract;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;
import com.midtrans.sdk.uikit.base.model.PaymentResponse;

public interface CreditCardDetailsContract extends BasePaymentContract, BaseCreditCardContract {
    boolean isBankPointEnabled();

    void onGetCardTokenSuccess(TokenDetailsResponse response);

    void onGetCardTokenFailure();

    void onGetBankPointSuccess(PointResponse response);

    void onGetBankPointFailure();

    void onGetTransactionStatusError(Throwable error);

    void onGetTransactionStatusSuccess(PaymentResponse transactionResponse);
}