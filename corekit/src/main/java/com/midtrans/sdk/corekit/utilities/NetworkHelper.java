package com.midtrans.sdk.corekit.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.network.MidtransRestAdapter;
import com.midtrans.sdk.corekit.core.api.merchant.MerchantApiManager;
import com.midtrans.sdk.corekit.core.api.midtrans.MidtransApiManager;
import com.midtrans.sdk.corekit.core.api.snap.SnapApiManager;

import static com.midtrans.sdk.corekit.utilities.ValidationHelper.isNotEmpty;

public class NetworkHelper {

    private static final String TAG = "NetworkHelper";

    public static <T> Boolean isValidForNetworkCall(Context context, MidtransCallback<T> callback) {
        if (isParamNotNull(context, callback)) {
            if (isNetworkAvailable(context))
                return true;
            else {
                Logger.error(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER);
                return false;
            }
        }
        return false;
    }

    public static <T> Boolean isParamNotNull(Context context, MidtransCallback<T> callback) {
        if (context == null) {
            Logger.error(TAG, Constants.MESSAGE_ERROR_MISSING_CONTEXT);
            return false;
        }

        if (callback == null) {
            Logger.error(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return false;
        }
        return true;
    }

    public static boolean isSuccess(int httpStatusCode, String responseStatusCode) {

        if (httpStatusCode == 200
                || httpStatusCode == 201
                || isResponseStatusCodeSuccess(responseStatusCode)) {
            return true;
        }

        return false;
    }

    public static boolean isResponseStatusCodeSuccess(String responseStatusCode) {

        if (isNotEmpty(responseStatusCode)
                && (responseStatusCode.equals(Constants.STATUS_CODE_200)
                || responseStatusCode.equals(Constants.STATUS_CODE_201))) {
            return true;
        }

        return false;
    }

    /**
     * Open utils for checking network status
     *
     * @return boolean based on network status
     */
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

    public static MidtransApiManager newMidtransServiceManager(String merchantServerUrl, int requestTimeOut) {
        if (TextUtils.isEmpty(merchantServerUrl)) {
            return null;
        }
        return new MidtransApiManager(MidtransRestAdapter.newMidtransApiService(merchantServerUrl, requestTimeOut));
    }
}