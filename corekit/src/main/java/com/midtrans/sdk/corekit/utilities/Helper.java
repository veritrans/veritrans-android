package com.midtrans.sdk.corekit.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    private static final String TAG = "Helper";

    public static final String CARD_TYPE_VISA = "VISA";
    public static final String CARD_TYPE_MASTERCARD = "MASTERCARD";
    public static final String CARD_TYPE_AMEX = "AMEX";
    public static final String CARD_TYPE_JCB = "JCB";

    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;

    public static <T> T parseToModel(String jsonToParse, Class<T> classOfT) {
        Gson gson = new GsonBuilder().create();
        T model = gson.fromJson(jsonToParse, classOfT);
        return model;
    }

    /**
     * Utility method which will help to close the keyboard.
     *
     * @param activity activity instance
     */
    public static void hideKeyboard(Activity activity) {
        try {
            Logger.info("hide keyboard");
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (RuntimeException e) {
            Logger.debug(TAG, "hideKeyboard():" + e.getMessage());
        }
    }

    /**
     * it will validate an given email-id.
     *
     * @param email email string
     * @return true if given email-id is valid else returns false
     */
    public static boolean isEmailValid(String email) {

        try {
            if (!TextUtils.isEmpty(email)) {
                Pattern pattern = Pattern.compile(Constants.EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
                if (pattern != null) {
                    Matcher matcher = pattern.matcher(email.trim());
                    return matcher.matches();
                }
            }
        } catch (RuntimeException e) {
            Logger.error(TAG, e.getMessage());
        }
        return false;
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
     * Started with '4' will be 'VISA' Started with '5' will be 'MASTERCARD' Started with '3' will
     * be 'AMEX'
     *
     * @param cardNo card number
     * @return card type
     */
    public static String getCardType(String cardNo) {
        try {
            if (cardNo.isEmpty()) {
                return "";
            } else {
                if (cardNo.charAt(0) == '4') {
                    return CARD_TYPE_VISA;
                } else if ((cardNo.charAt(0) == '5') && ((cardNo.charAt(1) == '1') || (cardNo.charAt(1) == '2')
                        || (cardNo.charAt(1) == '3') || (cardNo.charAt(1) == '4') || (cardNo.charAt(1) == '5'))) {
                    return CARD_TYPE_MASTERCARD;
                } else if ((cardNo.charAt(0) == '3') && ((cardNo.charAt(1) == '4') || (cardNo.charAt(1) == '7'))) {
                    return CARD_TYPE_AMEX;
                } else if (cardNo.startsWith("35") || cardNo.startsWith("2131") || cardNo.startsWith("1800")) {
                    return CARD_TYPE_JCB;
                } else {
                    return "";

                }
            }
        } catch (RuntimeException e) {
            return "";
        }
    }

    public static String getDeviceType(Activity activity) {
        String deviceType;
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);

        if (diagonalInches >= 6.5) {
            deviceType = "TABLET";
        } else {
            deviceType = "PHONE";
        }

        return deviceType;
    }
}