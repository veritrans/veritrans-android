package id.co.veritrans.sdk.core;

import android.util.Log;

/**
 *
 * helper class to display log messages on logcat.</p>
 * you can also disable log message for example in release mode of your application using instance of
 * {@link VeritransBuilder#enableLog(boolean)}
 *
 * Created by shivam on 10/20/15.
 */
public class Logger {


    public static void d(String tag, String message) {
        if (VeritransSDK.getVeritransSDK() != null
                && VeritransSDK.getVeritransSDK().isLogEnabled()) {
            Log.d("" + tag, "" + message);
        }
    }

    public static void d(String message) {
        if (VeritransSDK.getVeritransSDK() != null
                && VeritransSDK.getVeritransSDK().isLogEnabled()) {
            Log.d(Constants.TAG, "" + message);
        }
    }


    public static void i(String tag, String message) {
        if (VeritransSDK.getVeritransSDK() != null
                && VeritransSDK.getVeritransSDK().isLogEnabled()) {
            Log.i("" + tag, "" + message);
        }
    }

    public static void i(String message) {
        if (VeritransSDK.getVeritransSDK() != null
                && VeritransSDK.getVeritransSDK().isLogEnabled()) {
            Log.i(Constants.TAG, "" + message);
        }
    }


    public static void e(String tag, String message) {
        if (VeritransSDK.getVeritransSDK() != null
                && VeritransSDK.getVeritransSDK().isLogEnabled()) {
            Log.e("" + tag, "" + message);
        }
    }

    public static void e(String message) {
        if (VeritransSDK.getVeritransSDK() != null
                && VeritransSDK.getVeritransSDK().isLogEnabled()) {
            Log.e(Constants.TAG, "" + message);
        }
    }

}
