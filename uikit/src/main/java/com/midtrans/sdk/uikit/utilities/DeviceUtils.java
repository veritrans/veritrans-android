package com.midtrans.sdk.uikit.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.uikit.BuildConfig;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by ziahaqi on 10/23/17.
 */

public class DeviceUtils {
    private static final java.lang.String TAG = DeviceUtils.class.getSimpleName();

    public static String getConnectivityType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return "WIFI";
        } else {
            switch (tm.getNetworkType()) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return "4G";
                default:
                    return "UNKNOWN";
            }
        }
    }

    public static String getMemoryUsage() {
        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        return usedMemInMB + "MB";
    }


    public static String getTotalCpuUsage() {
        String usage = "0";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/stat"));
            String[] sa = reader.readLine().split("[ ]+", 9);
            long work = Long.parseLong(sa[1]) + Long.parseLong(sa[2]) + Long.parseLong(sa[3]);
            long total = work + Long.parseLong(sa[4]) + Long.parseLong(sa[5]) + Long.parseLong(sa[6]) + Long.parseLong(sa[7]);
            reader.close();

            float result = Math.round((work * 100 / (float) total)) * 100 / 100;
            usage = String.valueOf(result) + "%";
        } catch (Exception ex) {
            Logger.e(TAG, "cpu:" + ex.getMessage());
        }

        return usage;
    }

    public static String getDisplaySize(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float widthDpi = metrics.xdpi;
        float heightDpi = metrics.ydpi;
        float widthInches = widthPixels / widthDpi;
        float heightInches = heightPixels / heightDpi;

        String screenSize = widthInches + " x " + heightInches + " inches";
        return screenSize;
    }


    public static String[] getApplicationName(Activity activity) {
        String appInfo[] = new String[2];
        PackageManager m = activity.getPackageManager();
        try {
            String versionName = m.getPackageInfo(activity.getPackageName(), 0).versionName;
            String appName = activity.getApplication().getApplicationInfo().loadLabel(activity.getPackageManager()).toString();
            appInfo[0] = appName;
            appInfo[1] = versionName;
        } catch (Exception e) {
            Log.d(TAG, "appinfo:" + e.getMessage());
        }
        return appInfo;
    }
}
