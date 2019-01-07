package com.midtrans.sdk.corekit.core.payment;

import android.content.Context;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.MidtransSdk;
import com.midtrans.sdk.corekit.core.api.merchant.MerchantApiManager;
import com.midtrans.sdk.corekit.core.api.midtrans.MidtransApiManager;
import com.midtrans.sdk.corekit.core.api.snap.SnapApiManager;
import com.midtrans.sdk.corekit.utilities.NetworkHelper;

public class BaseGroupPayment {

    private static final String TAG = "PaymentGroupBase";

    static String getClientKey() {
        return MidtransSdk
                .getInstance()
                .getMerchantClientId();
    }

    static SnapApiManager getSnapApiManager() {
        return MidtransSdk
                .getInstance()
                .getSnapApiManager();
    }

    static MidtransApiManager getMidtransServiceManager() {
        return MidtransSdk
                .getInstance()
                .getMidtransApiManager();
    }

    static MerchantApiManager getMerchantApiManager() {
        return MidtransSdk
                .getInstance()
                .getMerchantApiManager();
    }

    static <T> Boolean isValidForNetworkCall(MidtransCallback<T> callback) {
        return NetworkHelper.isValidForNetworkCall(getSdkContext(), callback);
    }

    private static Context getSdkContext() {
        return MidtransSdk
                .getInstance()
                .getContext();
    }
}