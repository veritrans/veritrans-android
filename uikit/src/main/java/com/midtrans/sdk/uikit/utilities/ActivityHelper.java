package com.midtrans.sdk.uikit.utilities;

import android.app.Activity;

public class ActivityHelper {
    public static String getImagePath(Activity activity) {
        return "android.resource://" + activity.getPackageName() + "/";
    }
}
