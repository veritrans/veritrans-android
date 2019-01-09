package com.midtrans.sdk.corekit.utilities;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

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
    public static String getCardType(@NonNull String cardNo) {
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