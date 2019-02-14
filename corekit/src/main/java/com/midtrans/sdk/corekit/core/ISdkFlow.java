package com.midtrans.sdk.corekit.core;

import android.content.Context;

import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;

/**
 * Created by ziahaqi on 15/06/2016.
 */
public interface ISdkFlow {

    void runUIFlow(Context context, String snapToken);

    void runCreditCard(Context context, String snapToken);

    void runBankTransfer(Context context, String snapToken);

    void runPermataBankTransfer(Context context, String snapToken);

    void runMandiriBankTransfer(Context context, String snapToken);

    void runBniBankTransfer(Context context, String snapToken);

    void runBCABankTransfer(Context context, String snapToken);

    void runOtherBankTransfer(Context context, String snapToken);

    void runGoPay(Context context, String snapToken);

    void runBCAKlikPay(Context context, String snapToken);

    void runKlikBCA(Context context, String snapToken);

    void runMandiriClickpay(Context context, String snapToken);

    void runMandiriECash(Context context, String snapToken);

    void runCIMBClicks(Context context, String snapToken);

    void runBRIEpay(Context context, String snapToken);

    void runTelkomselCash(Context context, String snapToken);

    void runIndosatDompetku(Context context, String snapToken);

    void runXlTunai(Context context, String snapToken);

    void runIndomaret(Context context, String snapToken);

    void runKioson(Context context, String snapToken);

    void runGci(Context context, String snapToken);

    void runDanamonOnline(Context context, String tokenToken);

    void runAkulaku(Context context, String snapToken);

    void runAlfamart(Context context, String snapToken);

    void runCardRegistration(Context context, CardRegistrationCallback callback);
}
