package com.midtrans.sdk.uikit.utilities;

import android.content.Context;
import com.midtrans.sdk.uikit.R;

/**
 * Created by Fajar on 12/02/18.
 */

public class TimeUtils {
    public static String fromMillisToMinutes(Context context, long milliseconds) {
        if (context != null) {
            int seconds = (int) ((milliseconds / 1000) % 60);
            int minutes = (int) ((milliseconds / 1000) / 60);
            String result = "";
            if (minutes > 1) {
                result = minutes + " " + context.getString(R.string.minutes);
            } else if (minutes > 0) {
                result = minutes + " " + context.getString(R.string.minute);
            }
            if (seconds > 1) {
                result = result + " " + seconds + " " + context.getString(R.string.seconds);
            } else if (seconds > 0) {
                result = result + " " + seconds + " " + context.getString(R.string.second);
            }
            return result;
        }
        return "";
    }
}
