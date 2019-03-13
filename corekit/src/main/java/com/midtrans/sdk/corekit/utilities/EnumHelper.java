package com.midtrans.sdk.corekit.utilities;

import com.midtrans.sdk.corekit.base.enums.AcquiringBankType;
import com.midtrans.sdk.corekit.base.enums.AcquiringChannel;
import com.midtrans.sdk.corekit.base.enums.Authentication;
import com.midtrans.sdk.corekit.base.enums.CreditCardTransactionType;
import com.midtrans.sdk.corekit.base.enums.ExpiryTimeUnit;

public class EnumHelper {
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
        if (acquiringChannel == null) {
            return null;
        } else {
            return AcquiringChannel.MIGS;
        }
    }

    public static @CreditCardTransactionType
    String mappingToCreditCardType(@CreditCardTransactionType String type) {
        if (type == null) {
            return null;
        } else {
            if (type.equalsIgnoreCase(CreditCardTransactionType.AUTHORIZE)) {
                return CreditCardTransactionType.AUTHORIZE;
            } else {
                return CreditCardTransactionType.AUTHORIZE_CAPTURE;
            }
        }
    }

    public static String mappingToBankType(@AcquiringBankType String bank) {
        switch (bank) {
            case AcquiringBankType.CIMB:
                return AcquiringBankType.CIMB;
            case AcquiringBankType.BCA:
                return AcquiringBankType.BCA;
            case AcquiringBankType.MANDIRI:
                return AcquiringBankType.MANDIRI;
            case AcquiringBankType.BNI:
                return AcquiringBankType.BNI;
            case AcquiringBankType.BRI:
                return AcquiringBankType.BRI;
            case AcquiringBankType.DANAMON:
                return AcquiringBankType.DANAMON;
            case AcquiringBankType.MAYBANK:
                return AcquiringBankType.MAYBANK;
            case AcquiringBankType.MEGA:
                return AcquiringBankType.MEGA;
            case AcquiringBankType.NONE:
                return AcquiringBankType.NONE;
            default:
                return null;
        }
    }
}
