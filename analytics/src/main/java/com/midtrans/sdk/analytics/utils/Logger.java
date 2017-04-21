package com.midtrans.sdk.analytics.utils;

import android.util.Log;

/**
 * Created by rakawm on 4/21/17.
 */

public class Logger {
    private static final String TAG = "MidtransAnalytics";
    public static boolean enabled;

    /**
     * Log debug message.
     */
    public static void debug(String message) {
        if (enabled) {
            Log.d(TAG, message);
        }
    }

    /**
     * Log error message.
     */
    public static void error(String message, Throwable throwable) {
        if (enabled) {
            Log.e(TAG, message, throwable);
        }
    }
}