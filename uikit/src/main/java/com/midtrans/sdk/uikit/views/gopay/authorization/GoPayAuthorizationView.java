package com.midtrans.sdk.uikit.views.gopay.authorization;

import com.midtrans.sdk.corekit.models.TransactionResponse;

/**
 * Created by ziahaqi on 9/11/17.
 */

public interface GoPayAuthorizationView {

    void onVerificationCodeSuccess(TransactionResponse response);

    void onVerificationCodeFailure(TransactionResponse response);

    void onVerificationCodeError(Throwable error);

    void onResendSuccess();

    void onResendFailure();

    void onResenError();

}
