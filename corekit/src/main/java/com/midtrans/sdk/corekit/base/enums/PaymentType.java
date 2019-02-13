package com.midtrans.sdk.corekit.base.enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

import static com.midtrans.sdk.corekit.base.enums.PaymentType.AKULAKU;
import static com.midtrans.sdk.corekit.base.enums.PaymentType.ALFAMART;
import static com.midtrans.sdk.corekit.base.enums.PaymentType.BCA_KLIKPAY;
import static com.midtrans.sdk.corekit.base.enums.PaymentType.BCA_VA;
import static com.midtrans.sdk.corekit.base.enums.PaymentType.BNI_VA;
import static com.midtrans.sdk.corekit.base.enums.PaymentType.CIMB_CLICKS;
import static com.midtrans.sdk.corekit.base.enums.PaymentType.CREDIT_CARD;
import static com.midtrans.sdk.corekit.base.enums.PaymentType.DANAMON_ONLINE;
import static com.midtrans.sdk.corekit.base.enums.PaymentType.GOPAY;
import static com.midtrans.sdk.corekit.base.enums.PaymentType.INDOMARET;
import static com.midtrans.sdk.corekit.base.enums.PaymentType.KLIK_BCA;
import static com.midtrans.sdk.corekit.base.enums.PaymentType.MANDIRI_CLICKPAY;
import static com.midtrans.sdk.corekit.base.enums.PaymentType.MANDIRI_ECASH;
import static com.midtrans.sdk.corekit.base.enums.PaymentType.OTHER_VA;
import static com.midtrans.sdk.corekit.base.enums.PaymentType.PERMATA_VA;
import static com.midtrans.sdk.corekit.base.enums.PaymentType.TELKOMSEL_CASH;

@StringDef({
        CREDIT_CARD,
        BNI_VA,
        BCA_VA,
        PERMATA_VA,
        OTHER_VA,
        GOPAY,
        TELKOMSEL_CASH,
        MANDIRI_ECASH,
        BCA_KLIKPAY,
        CIMB_CLICKS,
        DANAMON_ONLINE,
        MANDIRI_CLICKPAY,
        KLIK_BCA,
        INDOMARET,
        ALFAMART,
        AKULAKU
})
@Retention(RetentionPolicy.SOURCE)
public @interface PaymentType {
    // Credit Card
    String CREDIT_CARD = "credit_card";
    // Bank Transfer - VA
    String BNI_VA = "bni_va";
    String BCA_VA = "bca_va";
    String PERMATA_VA = "permata_va";
    String OTHER_VA = "other_va";
    // Wallet
    String GOPAY = "gopay";
    String TELKOMSEL_CASH = "telkomsel_cash";
    String MANDIRI_ECASH = "mandiri_ecash";
    // Online Debit
    String BCA_KLIKPAY = "bca_klikpay";
    String CIMB_CLICKS = "cimb_clicks";
    String BRI_EPAY = "bri_epay";
    String DANAMON_ONLINE = "danamon_online";
    // Direct Debit
    String MANDIRI_CLICKPAY = "mandiri_clickpay";
    String KLIK_BCA = "bca_klikbca";
    // Convenience Store
    String INDOMARET = "indomaret";
    String ALFAMART = "alfamart";
    // Cardless Credit
    String AKULAKU = "akulaku";
}