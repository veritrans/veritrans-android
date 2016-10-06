package com.midtrans.sdk.corekit.core;

import android.util.Log;

import com.midtrans.sdk.corekit.BuildConfig;

/**
 * helper class to display log messages on logcat.</p> you can also disable log message for example
 * in release mode of your application using instance of {@link SdkCoreFlowBuilder#enableLog(boolean)}
 *
 * Created by shivam on 10/20/15.
 */
public class Logger {

    public static void d(String tag, String message) {
        if (BuildConfig.FLAVOR.equalsIgnoreCase("development")) {
            Log.d("" + tag, "" + message);
        }
    }

    public static void d(String message) {
        if (BuildConfig.FLAVOR.equalsIgnoreCase("development")) {
            Log.d(Constants.TAG, "" + message);
        }
    }


    public static void i(String tag, String message) {
        if (BuildConfig.FLAVOR.equalsIgnoreCase("development")) {
            Log.i("" + tag, "" + message);
        }
    }

    public static void i(String message) {
        if (BuildConfig.FLAVOR.equalsIgnoreCase("development")) {
            Log.i(Constants.TAG, "" + message);
        }
    }


    public static void e(String tag, String message) {
        if (BuildConfig.FLAVOR.equalsIgnoreCase("development")) {
            Log.e("" + tag, "" + message);
        }
    }

    public static void e(String message) {
        if (BuildConfig.FLAVOR.equalsIgnoreCase("development")) {
            Log.e(Constants.TAG, "" + message);
        }
    }

}
