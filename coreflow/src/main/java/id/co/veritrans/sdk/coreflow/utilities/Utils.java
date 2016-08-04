package id.co.veritrans.sdk.coreflow.utilities;

/**
 * Created by shivam on 10/26/15.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import id.co.veritrans.sdk.coreflow.R;
import id.co.veritrans.sdk.coreflow.core.Logger;

/**
 * Created by chetan on 14/05/15.
 */
public class Utils {

    public static final String CARD_TYPE_VISA = "VISA";
    public static final String CARD_TYPE_MASTERCARD = "MASTERCARD";
    public static final String CARD_TYPE_AMEX = "AMEX";
    public static final String CARD_TYPE_JCB = "JCB";

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService
                    (Context.CONNECTIVITY_SERVICE);
            if (connManager.getActiveNetworkInfo() != null && connManager.getActiveNetworkInfo()
                    .isAvailable() && connManager.getActiveNetworkInfo().isConnected()) {
                return true;
            }
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
            return false;
        }
        return false;
    }

    public static void hideKeyboard(Context context, View editText) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            if (editText != null) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                editText.clearFocus();
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }

    public static String getFormattedAmount(double amount) {
        try {
            String amountString = new DecimalFormat("#,###").format(amount);
            return amountString.replace(",",".");
        } catch (NumberFormatException e) {
            return "" + amount;
        } catch (NullPointerException e) {
            return "" + amount;
        }
    }

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

                    Logger.i("after parsing validity date becomes : " + date);
                    Logger.i("month is : " + month);
                    Logger.i("validity time is : " + validityTime);

                    return validityTime;

                } catch (ParseException ex) {
                    Logger.e("Error while parsing date : " + ex.getMessage());
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


    public static int dpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Get formatted card number;
     *
     * @param unformattedCardNumber unformatted credit card number
     * @return formatted card number.
     */
    public static String getFormattedCreditCardNumber(String unformattedCardNumber) {
        StringBuilder builder = new StringBuilder();
        if (unformattedCardNumber.length() == 16) {
            for (int i = 0; i < 16; i += 4) {
                builder.append(unformattedCardNumber.substring(i, i + 4));
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    /**
     * Return card type based on card number.
     * <p/>
     * Started with '4' will be 'VISA'
     * Started with '5' will be 'MASTERCARD'
     * Started with '3' will be 'AMEX'
     *
     * @param cardNo card number
     * @return card type
     */
    public static String getCardType(@NonNull  String cardNo) {
        if (cardNo.isEmpty() || cardNo.length() < 2) {
            return "";
        } else {
            if (cardNo.charAt(0) == '4') {
                return CARD_TYPE_VISA;
            } else if ((cardNo.charAt(0) == '5') && ((cardNo.charAt(1) == '1') || (cardNo.charAt(1) == '2')
                    || (cardNo.charAt(1) == '3') || (cardNo.charAt(1) == '4') || (cardNo.charAt(1) == '5'))) {
                return CARD_TYPE_MASTERCARD;

            } else if ((cardNo.charAt(0) == '3') && ((cardNo.charAt(1) == '4') || (cardNo.charAt(1) == '7'))) {
                return CARD_TYPE_AMEX;

            } else {
                return "";

            }
        }
    }
}