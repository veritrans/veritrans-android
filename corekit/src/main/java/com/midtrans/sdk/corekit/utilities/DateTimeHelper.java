package com.midtrans.sdk.corekit.utilities;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeHelper {

    //10 September 2015, 16:00
    public static String getValidityTime(String transactionTime) {

        if (transactionTime != null) {

            //2015-10-30 20:32:51
            String data[] = transactionTime.split(" ");
            if (data.length > 1) {

                try {
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(simpleDateFormat.parse(data[0]));
                    calendar.add(Calendar.DATE, 1);
                    String date = simpleDateFormat.format(calendar.getTime());

                    String splitedDate[] = date.split("-");
                    String month = getMonth(Integer.parseInt(splitedDate[1]));

                    String validityTime = "" + splitedDate[2] + " " + month + " " +
                            splitedDate[0] + ", " + data[1];

                    Logger.info("after parsing validity date becomes : " + date);
                    Logger.info("month is : " + month);
                    Logger.info("validity time is : " + validityTime);

                    return validityTime;

                } catch (ParseException ex) {
                    Logger.error("Error while parsing date : " + ex.getMessage());
                }

            }

        }

        return transactionTime;
    }

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

    public static String getFormattedTime(long time) {
        // Quoted "Z" to indicate UTC, no timezone offset
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        String nowAsISO = df.format(new Date(time));

        return nowAsISO;
    }

}