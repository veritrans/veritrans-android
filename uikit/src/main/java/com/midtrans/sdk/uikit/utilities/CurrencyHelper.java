package com.midtrans.sdk.uikit.utilities;

import android.app.Activity;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.enums.Currency;
import com.midtrans.sdk.uikit.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CurrencyHelper {
    public static String formatAmount(Activity context, double totalAmount, @Currency String currency) {
        String formattedAmount;

        if (TextUtils.isEmpty(currency)) {
            formattedAmount = context.getString(R.string.prefix_money, getFormattedAmount(totalAmount));
        } else {
            switch (currency) {
                case Currency.SGD:
                    formattedAmount = context.getString(R.string.prefix_money_sgd, getFormattedAmount(totalAmount));
                    break;

                default:
                    formattedAmount = context.getString(R.string.prefix_money, getFormattedAmount(totalAmount));
                    break;
            }
        }

        return formattedAmount;
    }

    public static String getFormattedAmount(double amount) {
        try {
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
            otherSymbols.setDecimalSeparator('.');
            otherSymbols.setGroupingSeparator(',');
            String amountString = new DecimalFormat("#,###.##", otherSymbols).format(amount);
            return amountString;
        } catch (NullPointerException | IllegalArgumentException e) {
            return "" + amount;
        }
    }
}
