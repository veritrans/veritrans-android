package com.midtrans.sdk.ui.utils;

/**
 * Created by ziahaqi on 2/24/17.
 */

public class CreditCardUtils {

    public static String getMaskedCardNumber(String maskedCard) {
        StringBuilder builder = new StringBuilder();
        String bulletMask = "●●●●●●";
        String newMaskedCard = maskedCard.replace("-", bulletMask);

        for (int i = 0; i < newMaskedCard.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                builder.append(' ');
                builder.append(newMaskedCard.charAt(i));
            } else {
                builder.append(newMaskedCard.charAt(i));
            }
        }
        return builder.toString();
    }

    public static String getMaskedExpDate() {
        String bulletMask = "●●";
        return bulletMask + " / " + bulletMask;
    }

    public static String getMaskedCardCvv() {
        return "●●●";
    }

}
