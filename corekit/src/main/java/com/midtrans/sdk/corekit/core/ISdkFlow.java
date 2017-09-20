package com.midtrans.sdk.corekit.core;

import android.content.Context;

import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;

/**
 * Created by ziahaqi on 15/06/2016.
 */
public interface ISdkFlow {

    void runUIFlow(Context context);

    void runCreditCard(Context context, String snapToken);

    void runBankTransfer(Context context, String snapToken);

    void runPermataBankTransfer(Context context);

    void runMandiriBankTransfer(Context context);

    void runBniBankTransfer(Context context);

    void runBCABankTransfer(Context context);

    void runOtherBankTransfer(Context context);

    void runBCAKlikPay(Context context);

    void runKlikBCA(Context context);

    void runMandiriClickpay(Context context);

    void runMandiriECash(Context context);

    void runCIMBClicks(Context context);

    void runBRIEpay(Context context);

    void runTelkomselCash(Context context);

    void runIndosatDompetku(Context context);

    void runXlTunai(Context context);

    void runIndomaret(Context context);

    void runKioson(Context context);

    void runGci(Context context);

    void runCardRegistration(Context context, CardRegistrationCallback callback);
}
