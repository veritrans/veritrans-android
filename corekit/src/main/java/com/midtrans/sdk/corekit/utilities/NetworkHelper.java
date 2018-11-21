package com.midtrans.sdk.corekit.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.network.MidtransRestAdapter;
import com.midtrans.sdk.corekit.core.merchant.MerchantApiManager;
import com.midtrans.sdk.corekit.core.snap.SnapApiManager;

public class NetworkHelper {

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService
                    (Context.CONNECTIVITY_SERVICE);
            if (connManager.getActiveNetworkInfo() != null && connManager.getActiveNetworkInfo()
                    .isAvailable() && connManager.getActiveNetworkInfo().isConnected()) {
                return true;
            }
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
            return false;
        }
        return false;
    }

    public static MerchantApiManager newMerchantServiceManager(String merchantServerUrl, int requestTimeOut) {
        if (TextUtils.isEmpty(merchantServerUrl)) {
            return null;
        }
        return new MerchantApiManager(MidtransRestAdapter.newMerchantApiService(merchantServerUrl, requestTimeOut));
    }

    public static SnapApiManager newSnapServiceManager(String merchantServerUrl, int requestTimeOut) {
        if (TextUtils.isEmpty(merchantServerUrl)) {
            return null;
        }
        return new SnapApiManager(MidtransRestAdapter.newSnapApiService(merchantServerUrl, requestTimeOut));
    }
}