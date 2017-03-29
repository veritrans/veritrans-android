package com.midtrans.sdk.core.utils;

import android.support.annotation.NonNull;

/**
 * Created by rakawm on 1/24/17.
 */
public class CardUtilities {
    public static final String CARD_TYPE_VISA = "VISA";
    public static final String CARD_TYPE_MASTERCARD = "MASTERCARD";
    public static final String CARD_TYPE_AMEX = "AMEX";
    public static final String CARD_TYPE_JCB = "JCB";

    /**
     * It will validate an given card number using Luhn Algorithm.
     *
     * @param ccNumber credit card number
     * @return true if given card number is valid else returns false.
     */
    public static boolean isValidCardNumber(String ccNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
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
            } else if (cardNo.startsWith("35") || cardNo.startsWith("2131") || cardNo.startsWith("1800")) {
                return CARD_TYPE_JCB;
            }
        }
        return "";
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
        return builder.toString().trim();
    }
}
