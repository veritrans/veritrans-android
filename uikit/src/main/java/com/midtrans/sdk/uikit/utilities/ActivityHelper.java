package com.midtrans.sdk.uikit.utilities;

import android.app.Activity;
import android.util.DisplayMetrics;

public class ActivityHelper {

    public static final String TYPE_PHONE = "PHONE";
    public static final String TYPE_TABLET = "TABLET";

    public static String getImagePath(Activity activity) {
        return "android.resource://" + activity.getPackageName() + "/";
    }
    public static String getDeviceType(Activity activity) {
        String deviceType;
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);

        if (diagonalInches >= 6.5) {
            deviceType = TYPE_TABLET;
        } else {
            deviceType = TYPE_PHONE;
        }

        return deviceType;
    }
}
