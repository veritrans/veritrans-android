package com.midtrans.sdk.corekit.utilities;

import android.util.Log;

import com.midtrans.sdk.corekit.BuildConfig;

public class Logger {
    public static boolean enabled = false;

    public static void debug(String tag, String message) {
        if (enabled) {
            Log.d("" + tag, "" + message);
        }
    }

    public static void debug(String message) {
        if (enabled) {
            Log.d(Constants.TAG, "" + message);
        }
    }

    public static void info(String tag, String message) {
        if (enabled) {
            Log.i("" + tag, "" + message);
        }
    }

    public static void info(String message) {
        if (enabled) {
            Log.i(Constants.TAG, "" + message);
        }
    }

    public static void error(String tag, String message) {
        if (BuildConfig.FLAVOR.equalsIgnoreCase("development")) {
            Log.e("" + tag, "" + message);
        }
    }

    public static void error(String tag, Throwable throwable) {
        if (BuildConfig.FLAVOR.equalsIgnoreCase("development")) {
            Log.e("" + tag, "exeption:", throwable);
        }
    }

    public static void error(String message) {
        if (enabled) {
            Log.e(Constants.TAG, "" + message);
        }
    }

}