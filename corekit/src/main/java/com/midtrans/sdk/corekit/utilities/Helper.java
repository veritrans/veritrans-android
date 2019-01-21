package com.midtrans.sdk.corekit.utilities;

import android.app.Activity;
import android.util.DisplayMetrics;

import com.midtrans.sdk.corekit.base.enums.Authentication;
import com.midtrans.sdk.corekit.base.enums.BankType;
import com.midtrans.sdk.corekit.base.enums.CreditCardType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    private static final String CIMB = "cimb";
    private static final String BCA = "bca";
    private static final String MANDIRI = "mandiri";
    private static final String BNI = "bni";
    private static final String BRI = "bri";
    private static final String DANAMON = "danamon";
    private static final String MAYBANK = "maybank";
    private static final String MEGA = "mega";
    private static final String NONE = "offline";

    private static String AUTHORIZE = "authorize";
    private static String AUTHORIZE_CAPTURE = "authorize_capture";

    private static final String AUTHENTICATION_TYPE_RBA = "rba";
    private static final String AUTHENTICATION_TYPE_3DS = "3ds";
    private static final String AUTHENTICATION_TYPE_NONE = "none";

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

    public static Map<String, List<Integer>> mappingHashmapToBankTypeForSet(Map<BankType, List<Integer>> installmentMap) {
        HashMap<String, List<Integer>> mappedHash = new HashMap<>();
        for (Map.Entry<BankType, List<Integer>> entry : installmentMap.entrySet()) {
            mappedHash.put(Objects.requireNonNull(mappingToBankType(entry.getKey())), entry.getValue());
        }
        return mappedHash;
    }

    public static Map<BankType, List<Integer>> mappingHashmapToBankTypeForGet(Map<String, List<Integer>> installmentMap) {
        HashMap<BankType, List<Integer>> mappedHash = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : installmentMap.entrySet()) {
            mappedHash.put(Objects.requireNonNull(mappingToBankType(entry.getKey())), entry.getValue());
        }
        return mappedHash;
    }

    public static Authentication mappingToCreditCardAuthentication(String type) {
        if (type.equalsIgnoreCase(AUTHENTICATION_TYPE_3DS)) {
            return Authentication.AUTH_3DS;
        } else if (type.equalsIgnoreCase(AUTHENTICATION_TYPE_RBA)) {
            return Authentication.AUTH_RBA;
        } else {
            return Authentication.AUTH_NONE;
        }
    }

    public static String mappingToCreditCardAuthentication(Authentication authentication) {
        if (authentication == Authentication.AUTH_3DS) {
            return AUTHENTICATION_TYPE_3DS;
        } else if (authentication == Authentication.AUTH_RBA) {
            return AUTHENTICATION_TYPE_RBA;
        } else {
            return AUTHENTICATION_TYPE_NONE;
        }
    }

    public static CreditCardType mappingToCreditCardType(String type) {
        if (type.equalsIgnoreCase(AUTHORIZE)) {
            return CreditCardType.AUTHORIZE;
        } else {
            return CreditCardType.AUTHORIZE_CAPTURE;
        }
    }

    public static String mappingToCreditCardType(CreditCardType type) {
        if (type == CreditCardType.AUTHORIZE) {
            return AUTHORIZE;
        } else {
            return AUTHORIZE_CAPTURE;
        }
    }

    public static String mappingToBankType(BankType bank) {
        if (bank == BankType.CIMB) {
            return CIMB;
        } else if (bank == BankType.BCA) {
            return BCA;
        } else if (bank == BankType.MANDIRI) {
            return MANDIRI;
        } else if (bank == BankType.BNI) {
            return BNI;
        } else if (bank == BankType.BRI) {
            return BRI;
        } else if (bank == BankType.DANAMON) {
            return DANAMON;
        } else if (bank == BankType.MAYBANK) {
            return MAYBANK;
        } else if (bank == BankType.MEGA) {
            return MEGA;
        } else if (bank == BankType.NONE) {
            return NONE;
        } else {
            return null;
        }
    }

    public static BankType mappingToBankType(String bank) {
        if (bank.equalsIgnoreCase(CIMB)) {
            return BankType.CIMB;
        } else if (bank.equalsIgnoreCase(BCA)) {
            return BankType.BCA;
        } else if (bank.equalsIgnoreCase(MANDIRI)) {
            return BankType.MANDIRI;
        } else if (bank.equalsIgnoreCase(BNI)) {
            return BankType.BNI;
        } else if (bank.equalsIgnoreCase(BRI)) {
            return BankType.BRI;
        } else if (bank.equalsIgnoreCase(DANAMON)) {
            return BankType.DANAMON;
        } else if (bank.equalsIgnoreCase(MAYBANK)) {
            return BankType.MAYBANK;
        } else if (bank.equalsIgnoreCase(MEGA)) {
            return BankType.MEGA;
        } else if (bank.equalsIgnoreCase(NONE)) {
            return BankType.NONE;
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