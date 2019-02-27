package com.midtrans.sdk.uikit.utilities;

import android.content.Context;

import com.midtrans.sdk.uikit.R;

public class DateTimeHelper {

    public static String getMonth(int monthValue) {

        switch (monthValue) {

            case 1:
                return "January";

            case 2:
                return "February";

            case 3:
                return "March";

            case 4:
                return "April";

            case 5:
                return "May";

            case 6:
                return "June";

            case 7:
                return "July";

            case 8:
                return "August";

            case 9:
                return "September";

            case 10:
                return "October";

            case 11:
                return "November";

            case 12:
                return "December";

            default:
                return "Invalid Month";
        }
    }

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
