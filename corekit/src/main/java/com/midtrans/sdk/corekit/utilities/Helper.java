package com.midtrans.sdk.corekit.utilities;

import android.app.Activity;
import android.util.DisplayMetrics;

import com.midtrans.sdk.corekit.base.enums.AcquiringBankType;
import com.midtrans.sdk.corekit.base.enums.AcquiringChannel;
import com.midtrans.sdk.corekit.base.enums.Authentication;
import com.midtrans.sdk.corekit.base.enums.CreditCardTransactionType;
import com.midtrans.sdk.corekit.base.enums.ExpiryTimeUnit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Map<String, List<Integer>> mappingMapToBankTypeForSet(Map<String, List<Integer>> installmentMap) {
        HashMap<String, List<Integer>> mappedHash = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : installmentMap.entrySet()) {
            mappedHash.put(mappingToBankType(entry.getKey()), entry.getValue());
        }
        return mappedHash;
    }

    public static @ExpiryTimeUnit
    String mappingToExpiryTimeUnit(@ExpiryTimeUnit String unit) {
        switch (unit) {
            case ExpiryTimeUnit.DAY:
                return ExpiryTimeUnit.DAY;
            case ExpiryTimeUnit.HOUR:
                return ExpiryTimeUnit.HOUR;
            case ExpiryTimeUnit.MINUTE:
                return ExpiryTimeUnit.MINUTE;
        }
        return null;
    }

    public static @Authentication
    String mappingToCreditCardAuthentication(@Authentication String authentication, boolean secure) {
        if (authentication.equalsIgnoreCase(Authentication.AUTH_3DS) && secure) {
            return Authentication.AUTH_3DS;
        } else if (authentication.equalsIgnoreCase(Authentication.AUTH_RBA) && !secure) {
            return Authentication.AUTH_RBA;
        } else {
            return Authentication.AUTH_NONE;
        }
    }

    public static @AcquiringChannel
    String mappingToAcquiringChannel(@AcquiringChannel String acquiringChannel) {
        if (acquiringChannel.equalsIgnoreCase(AcquiringChannel.MIGS) || acquiringChannel == null) {
            return AcquiringChannel.MIGS;
        } else {
            return null;
        }
    }

    public static @CreditCardTransactionType
    String mappingToCreditCardType(@CreditCardTransactionType String type) {
        if (type.equalsIgnoreCase(CreditCardTransactionType.AUTHORIZE)) {
            return CreditCardTransactionType.AUTHORIZE;
        } else {
            return CreditCardTransactionType.AUTHORIZE_CAPTURE;
        }
    }

    public static String mappingToBankType(@AcquiringBankType String bank) {
        if (bank.equals(AcquiringBankType.CIMB)) {
            return AcquiringBankType.CIMB;
        } else if (bank.equals(AcquiringBankType.BCA)) {
            return AcquiringBankType.BCA;
        } else if (bank.equals(AcquiringBankType.MANDIRI)) {
            return AcquiringBankType.MANDIRI;
        } else if (bank.equals(AcquiringBankType.BNI)) {
            return AcquiringBankType.BNI;
        } else if (bank.equals(AcquiringBankType.BRI)) {
            return AcquiringBankType.BRI;
        } else if (bank.equals(AcquiringBankType.DANAMON)) {
            return AcquiringBankType.DANAMON;
        } else if (bank.equals(AcquiringBankType.MAYBANK)) {
            return AcquiringBankType.MAYBANK;
        } else if (bank.equals(AcquiringBankType.MEGA)) {
            return AcquiringBankType.MEGA;
        } else if (bank.equals(AcquiringBankType.NONE)) {
            return AcquiringBankType.NONE;
        } else {
            return null;
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