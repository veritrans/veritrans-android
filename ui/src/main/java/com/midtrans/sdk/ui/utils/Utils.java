package com.midtrans.sdk.ui.utils;

import android.text.TextUtils;

import com.midtrans.sdk.core.models.snap.CreditCard;
import com.midtrans.sdk.core.models.snap.transaction.SnapTransaction;

import java.text.DecimalFormat;

/**
 * Created by ziahaqi on 2/22/17.
 */

public class Utils {

    /**
     *
     * @param amount
     * @return formatted  amount
     */
    public static String getFormattedAmount(double amount) {
        try {
            String amountString = new DecimalFormat("#,###").format(amount);
            return amountString.replace(",", ".");
        } catch (NumberFormatException e) {
            return "" + amount;
        } catch (NullPointerException e) {
            return "" + amount;
        }
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


    public static boolean isCreditCardOneClickMode(CreditCard creditCard) {
        if (creditCard != null) {
            if (creditCard.saveCard && creditCard.secure) {
                return true;
            }
        }
        return false;
    }
}
