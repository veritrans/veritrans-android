package com.midtrans.sdk.corekit.base.enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

import static com.midtrans.sdk.corekit.base.enums.AcquiringBankType.BCA;
import static com.midtrans.sdk.corekit.base.enums.AcquiringBankType.BNI;
import static com.midtrans.sdk.corekit.base.enums.AcquiringBankType.BRI;
import static com.midtrans.sdk.corekit.base.enums.AcquiringBankType.CIMB;
import static com.midtrans.sdk.corekit.base.enums.AcquiringBankType.DANAMON;
import static com.midtrans.sdk.corekit.base.enums.AcquiringBankType.MANDIRI;
import static com.midtrans.sdk.corekit.base.enums.AcquiringBankType.MAYBANK;
import static com.midtrans.sdk.corekit.base.enums.AcquiringBankType.MEGA;
import static com.midtrans.sdk.corekit.base.enums.AcquiringBankType.NONE;

@StringDef({CIMB, BCA, MANDIRI, BNI, BRI, DANAMON, MAYBANK, MEGA, NONE})
@Retention(RetentionPolicy.SOURCE)
public @interface AcquiringBankType {
    String CIMB = "cimb";
    String BCA = "bca";
    String MANDIRI = "mandiri";
    String BNI = "bni";
    String BRI = "bri";
    String DANAMON = "danamon";
    String MAYBANK = "maybank";
    String MEGA = "mega";
    String NONE = "offline";
}