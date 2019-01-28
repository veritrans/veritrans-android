package com.midtrans.sdk.corekit.utilities;

import android.app.Activity;
import android.util.DisplayMetrics;

import com.midtrans.sdk.corekit.base.enums.AcquiringBankType;
import com.midtrans.sdk.corekit.base.enums.AcquiringChannel;
import com.midtrans.sdk.corekit.base.enums.Authentication;
import com.midtrans.sdk.corekit.base.enums.CreditCardTransactionType;

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

    private static final String ACQUIRING_CHANNEL_MIGS = "MIGS";

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

    public static Map<String, List<Integer>> mappingMapToBankTypeForSet(Map<AcquiringBankType, List<Integer>> installmentMap) {
        HashMap<String, List<Integer>> mappedHash = new HashMap<>();
        for (Map.Entry<AcquiringBankType, List<Integer>> entry : installmentMap.entrySet()) {
            mappedHash.put(Objects.requireNonNull(mappingToBankType(entry.getKey())), entry.getValue());
        }
        return mappedHash;
    }

    public static Map<AcquiringBankType, List<Integer>> mappingMapToBankTypeForGet(Map<String, List<Integer>> installmentMap) {
        HashMap<AcquiringBankType, List<Integer>> mappedHash = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : installmentMap.entrySet()) {
            mappedHash.put(Objects.requireNonNull(mappingToBankType(entry.getKey())), entry.getValue());
        }
        return mappedHash;
    }

    public static Authentication mappingToCreditCardAuthentication(String type, boolean secure) {
        if (type.equalsIgnoreCase(AUTHENTICATION_TYPE_3DS)) {
            return Authentication.AUTH_3DS;
        } else if (type.equalsIgnoreCase(AUTHENTICATION_TYPE_RBA) && secure) {
            return Authentication.AUTH_RBA_SECURE;
        } else if (type.equalsIgnoreCase(AUTHENTICATION_TYPE_RBA) && !secure) {
            return Authentication.AUTH_RBA;
        } else {
            return Authentication.AUTH_NONE;
        }
    }

    public static String mappingToCreditCardAuthentication(Authentication authentication) {
        if (authentication == Authentication.AUTH_3DS) {
            return AUTHENTICATION_TYPE_3DS;
        } else if (authentication == Authentication.AUTH_RBA || authentication == Authentication.AUTH_RBA_SECURE) {
            return AUTHENTICATION_TYPE_RBA;
        } else {
            return AUTHENTICATION_TYPE_NONE;
        }
    }

    public static String mappingToAcquiringChannel(AcquiringChannel acquiringChannel) {
        if (acquiringChannel == AcquiringChannel.MIGS) {
            return ACQUIRING_CHANNEL_MIGS;
        } else {
            return null;
        }
    }

    public static AcquiringChannel mappingToAcquiringChannel(String acquiringChannel) {
        if (acquiringChannel.equalsIgnoreCase(ACQUIRING_CHANNEL_MIGS)) {
            return AcquiringChannel.MIGS;
        } else {
            return null;
        }
    }

    public static CreditCardTransactionType mappingToCreditCardType(String type) {
        if (type.equalsIgnoreCase(AUTHORIZE)) {
            return CreditCardTransactionType.AUTHORIZE;
        } else {
            return CreditCardTransactionType.AUTHORIZE_CAPTURE;
        }
    }

    public static String mappingToCreditCardType(CreditCardTransactionType type) {
        if (type != null && type == CreditCardTransactionType.AUTHORIZE) {
            return AUTHORIZE;
        } else {
            return AUTHORIZE_CAPTURE;
        }
    }

    public static String mappingToBankType(AcquiringBankType bank) {
        if (bank == AcquiringBankType.CIMB) {
            return CIMB;
        } else if (bank == AcquiringBankType.BCA) {
            return BCA;
        } else if (bank == AcquiringBankType.MANDIRI) {
            return MANDIRI;
        } else if (bank == AcquiringBankType.BNI) {
            return BNI;
        } else if (bank == AcquiringBankType.BRI) {
            return BRI;
        } else if (bank == AcquiringBankType.DANAMON) {
            return DANAMON;
        } else if (bank == AcquiringBankType.MAYBANK) {
            return MAYBANK;
        } else if (bank == AcquiringBankType.MEGA) {
            return MEGA;
        } else if (bank == AcquiringBankType.NONE) {
            return NONE;
        } else {
            return null;
        }
    }

    public static AcquiringBankType mappingToBankType(String bank) {
        if (bank.equalsIgnoreCase(CIMB)) {
            return AcquiringBankType.CIMB;
        } else if (bank.equalsIgnoreCase(BCA)) {
            return AcquiringBankType.BCA;
        } else if (bank.equalsIgnoreCase(MANDIRI)) {
            return AcquiringBankType.MANDIRI;
        } else if (bank.equalsIgnoreCase(BNI)) {
            return AcquiringBankType.BNI;
        } else if (bank.equalsIgnoreCase(BRI)) {
            return AcquiringBankType.BRI;
        } else if (bank.equalsIgnoreCase(DANAMON)) {
            return AcquiringBankType.DANAMON;
        } else if (bank.equalsIgnoreCase(MAYBANK)) {
            return AcquiringBankType.MAYBANK;
        } else if (bank.equalsIgnoreCase(MEGA)) {
            return AcquiringBankType.MEGA;
        } else if (bank.equalsIgnoreCase(NONE)) {
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