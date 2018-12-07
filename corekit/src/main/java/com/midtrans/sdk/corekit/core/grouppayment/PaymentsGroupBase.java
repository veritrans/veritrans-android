package com.midtrans.sdk.corekit.core.grouppayment;

import android.content.Context;

import com.midtrans.sdk.corekit.core.MidtransSdk;
import com.midtrans.sdk.corekit.core.snap.SnapApiManager;

public class PaymentsGroupBase {

    public static SnapApiManager getSnapApiManager() {
        return MidtransSdk
                .getInstance()
                .getSnapApiManager();
    }

    public static Context getSdkContext() {
        return MidtransSdk
                .getInstance()
                .getContext();
    }
}
