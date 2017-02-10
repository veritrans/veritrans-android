package com.midtrans.sdk.core.utils;

import android.util.Log;

/**
 * Created by rakawm on 10/19/16.
 */

public class Logger {
    public static boolean enabled;

    /**
     * Log debug message.
     */
    public static void debug(String message) {
        if (enabled) {
            Log.d(Constants.SDK_TAG, message);
        }
    }

    /**
     * Log error message.
     */
    public static void error(String message, Throwable throwable) {
        if (enabled) {
            Log.e(Constants.SDK_TAG, message, throwable);
        }
    }
}
