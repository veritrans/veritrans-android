package com.midtrans.sdk.corekit.base.enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

import static com.midtrans.sdk.corekit.base.enums.BankType.BCA;
import static com.midtrans.sdk.corekit.base.enums.BankType.BNI;
import static com.midtrans.sdk.corekit.base.enums.BankType.BNI_DEBIT_ONLINE;
import static com.midtrans.sdk.corekit.base.enums.BankType.BRI;
import static com.midtrans.sdk.corekit.base.enums.BankType.CIMB;
import static com.midtrans.sdk.corekit.base.enums.BankType.DANAMON;
import static com.midtrans.sdk.corekit.base.enums.BankType.MANDIRI;
import static com.midtrans.sdk.corekit.base.enums.BankType.MANDIRI_DEBIT;
import static com.midtrans.sdk.corekit.base.enums.BankType.MAYBANK;
import static com.midtrans.sdk.corekit.base.enums.BankType.MEGA;
import static com.midtrans.sdk.corekit.base.enums.BankType.PERMATA;

@StringDef({CIMB, BCA, MANDIRI, BNI, BRI, DANAMON, MAYBANK, MEGA, MANDIRI_DEBIT, BNI_DEBIT_ONLINE, PERMATA})
@Retention(RetentionPolicy.SOURCE)
public @interface BankType {
    String CIMB = "cimb";
    String BCA = "bca";
    String MANDIRI = "mandiri";
    String BNI = "bni";
    String BRI = "bri";
    String DANAMON = "danamon";
    String MANDIRI_DEBIT = "mandiri_debit";
    String BNI_DEBIT_ONLINE = "bni_debit_online";
    String PERMATA = "permata";
    String MAYBANK = "maybank";
    String MEGA = "mega";
}