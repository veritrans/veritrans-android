package id.co.veritrans.sdk.widgets.utils;

import android.support.annotation.NonNull;

/**
 * Credit card utilities.
 *
 * @author rakawm
 */
public class CardUtils {

    public static final String CARD_TYPE_VISA = "VISA";
    public static final String CARD_TYPE_MASTERCARD = "MASTERCARD";
    public static final String CARD_TYPE_AMEX = "AMEX";
    public static final String CARD_TYPE_JCB = "JCB";

    /**
     * Return validation of a given card number.
     *
     * @param cardNumber credit card number
     * @return true if given card number is valid else returns false.
     */
    public static boolean isValidCardNumber(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }

    /**
     * Return card type based on card number.
     *
     * Started with '4' will be 'VISA' Started with '5' will be 'MASTERCARD' Started with '34' or
     * '37' will be 'AMEX' Started with '35', '2131' or '1800' will be 'JCB'
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
            } else {
                return "";

            }
        }
    }
}
