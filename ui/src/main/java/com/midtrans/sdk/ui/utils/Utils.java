package com.midtrans.sdk.ui.utils;

import android.content.Context;
import android.text.TextUtils;

import com.midtrans.sdk.analytics.MidtransAnalytics;
import com.midtrans.sdk.core.models.snap.CreditCard;
import com.midtrans.sdk.core.models.snap.SnapCustomerDetails;
import com.midtrans.sdk.core.models.snap.transaction.SnapTransaction;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.MidtransUiCallback;
import com.midtrans.sdk.ui.models.PaymentResult;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by ziahaqi on 2/22/17.
 */

public class Utils {

    /**
     * @param amount
     * @return formatted  amount
     */
    public static String getFormattedAmount(double amount) {
        try {
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
            otherSymbols.setDecimalSeparator('.');
            otherSymbols.setGroupingSeparator(',');
            return new DecimalFormat("#,###", otherSymbols).format(amount);
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

    public static SnapCustomerDetails createSnapCustomerDetails(String newEmail) {
        SnapTransaction transaction = MidtransUi.getInstance().getTransaction();
        String fullName = TextUtils.isEmpty(transaction.customerDetails.lastName) ? transaction.customerDetails.firstName : getFullName(transaction.customerDetails.firstName, transaction.customerDetails.lastName);
        String userEmail = newEmail;

        if (TextUtils.isEmpty(userEmail)) {
            userEmail = transaction.customerDetails.email;
        }
        return new SnapCustomerDetails(fullName, userEmail, transaction.customerDetails.phone);
    }

    public static String getFullName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }

    /**
     * Get device identifier using SDK context.
     *
     * @return device identifier
     */
    public static String getDeviceId(Context context) {
        String deviceId = "UNKNOWN";
        try {
            deviceId = Installation.id(context);
        } catch (Exception ex) {
            Logger.e("Utils", ex.toString());
        }
        return deviceId;
    }

    public static void sendPaymentResult(PaymentResult paymentResult) {
        MidtransUiCallback paymentCallback = MidtransUi.getInstance().getPaymentCallback();
        if (paymentCallback != null) {
            paymentCallback.onFinished(paymentResult);
        }
    }

    public static void trackEvent(String eventName) {
        MidtransAnalytics.getInstance().trackEvent(MidtransUi.getInstance().getTransaction().token, eventName);
    }

    public static void trackEvent(String eventName, String cardPaymentMode) {
        MidtransAnalytics.getInstance().trackEvent(MidtransUi.getInstance().getTransaction().token, eventName, cardPaymentMode);
    }
}
