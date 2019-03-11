package com.midtrans.sdk.corekit.utilities;

import android.content.Context;

import com.midtrans.sdk.corekit.R;
import com.securepreferences.SecurePreferences;

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

    public static SecurePreferences newPreferences(Context context, String name) {

        SecurePreferences preferences = new SecurePreferences(context, context.getString(R.string.PREFERENCE_PASSWORD), name);
        int prefVersion;
        try {
            prefVersion = preferences.getInt(Constants.KEY_PREFERENCES_VERSION, 0);
        } catch (ClassCastException e) {
            prefVersion = 0;
        }
        if (prefVersion == 0 || prefVersion < Constants.PREFERENCES_VERSION) {
            SecurePreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.putInt(Constants.KEY_PREFERENCES_VERSION, Constants.PREFERENCES_VERSION);
            editor.apply();
        }

        return preferences;
    }
}