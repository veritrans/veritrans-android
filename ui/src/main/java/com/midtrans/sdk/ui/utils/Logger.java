package com.midtrans.sdk.ui.utils;

import android.util.Log;

/**
 * Created by ziahaqi on 2/19/17.
 */

public class Logger {
    public static boolean enabled = true;

    /**
     * Log debug message.
     */
    public static void d(String tag, String message) {
        if (enabled) {
            Log.d(tag, message);
        }
    }

    /**
     * Log error message.
     */
    public static void e(String tag, String message) {
        if (enabled) {
            Log.e(tag, message);
        }
    }
}
