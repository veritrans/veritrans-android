package com.midtrans.sdk.uikit.view.method.gopay.instruction;

import com.midtrans.sdk.corekit.core.api.snap.model.payment.PaymentStatusResponse;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;

public interface GopayInstructionContract extends BasePaymentContract {

    void onGetPaymentStatusError(Throwable throwable);

    void onGetPaymentStatusSuccess(PaymentStatusResponse response);
}