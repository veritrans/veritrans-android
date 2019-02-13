package com.midtrans.sdk.uikit.utilities;

import android.content.Context;
import android.net.ConnectivityManager;

import com.midtrans.sdk.corekit.utilities.Logger;

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
}