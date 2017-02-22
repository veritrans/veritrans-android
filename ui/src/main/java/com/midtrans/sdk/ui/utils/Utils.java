package com.midtrans.sdk.ui.utils;

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
}
