package com.midtrans.sdk.uikit.utilities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.utilities.Constants;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.base.callback.PaymentResult;

import java.util.List;

import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_INVALID_DATA_SUPPLIED;
import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_SNAP_TOKEN;

public class MidtransKitHelper {

    /**
     * This method will look up app based on package name in Google Play Store
     * Extra steps are taken to make sure that only Google Play Store will be opened
     * even though there's another app that can handle "market" intent
     * Based from https://stackoverflow.com/a/28090925
     *
     * @param packageName package name of app
     */
    public static void openAppInPlayStore(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + packageName));
        boolean marketFound = false;

        // find all applications able to handle our intent
        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(intent, 0);
        for (ResolveInfo otherApp : otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName.equals("com.android.vending")) {
                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );

                // make sure it does NOT open in the stack of your activity
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // task reparenting if needed
                intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                intent.setComponent(componentName);
                context.startActivity(intent);
                marketFound = true;
                break;
            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            context.startActivity(webIntent);
        }
    }

    public static boolean isAppInstalled(Context context, String appPackage) {
        try {
            context.getPackageManager().getApplicationInfo(appPackage, 0);
            return true;
        } catch (PackageManager.NameNotFoundException exception) {
            return false;
        }
    }

    public static Boolean isValidForStartMidtransKit(Context context, CheckoutTransaction checkoutTransaction, PaymentResult callback) {
        if (checkoutTransaction == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_INVALID_DATA_SUPPLIED));
            return false;
        } else {
            return isValidForStartMidtransKit(context, callback);
        }
    }

    public static Boolean isValidForStartMidtransKit(Context context, String token, PaymentResult callback) {
        if (token == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_SNAP_TOKEN));
            return false;
        } else {
            return isValidForStartMidtransKit(context, callback);
        }
    }

    public static Boolean isValidForStartMidtransKit(Context context, PaymentResult callback) {
        if (isParamNotNull(context, callback)) {
            if (NetworkHelper.isNetworkAvailable(context))
                return true;
            else {
                callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
                return false;
            }
        }
        return false;
    }

    public static Boolean isParamNotNull(Context context, PaymentResult callback) {
        if (context == null) {
            Logger.error(Constants.MESSAGE_ERROR_MISSING_CONTEXT);
            return false;
        }

        if (callback == null) {
            Logger.error(Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return false;
        }
        return true;
    }

}