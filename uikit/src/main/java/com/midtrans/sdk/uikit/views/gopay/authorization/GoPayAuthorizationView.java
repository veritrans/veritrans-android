package com.midtrans.sdk.uikit.views.gopay.authorization;

/**
 * Created by ziahaqi on 9/11/17.
 */

public interface GoPayAuthorizationView {

    void onVerificationCodeSuccess();

    void onVerificationCodeFailure();

    void onVerificationCodeError();

    void onResendSuccess();

    void onResendFailure();

    void onResenError();

}
