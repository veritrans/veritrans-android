package com.midtrans.sdk.widgets.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.midtrans.sdk.corekit.core.Logger;

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

    /**
     * Utility method which will help to close the keyboard.
     *
     * @param context Context instance
     */
    public static void hideKeyboard(Context context) {
        try {
            Logger.i("hide keyboard");
            View view = ((Activity)context).getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * shows keyboard on screen forcefully.
     *
     * @param context  Context instance
     * @param editText  edittext instance
     */
    public static void showKeyboard(Context context, EditText editText) {
        Logger.i("show keyboard");
        if (editText != null) {
            editText.requestFocus();
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

    }
}
