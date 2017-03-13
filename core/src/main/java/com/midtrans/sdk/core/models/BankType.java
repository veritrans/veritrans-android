package com.midtrans.sdk.core.models;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rakawm on 2/8/17.
 */

public class BankType {
    public static final String CIMB = "cimb";
    public static final String BCA = "bca";
    public static final String MANDIRI = "mandiri";
    public static final String BNI = "bni";
    public static final String MAYBANK = "maybank";
    public static final String BRI = "bri";
    public static final String DANAMON = "danamon";
    public static final String MANDIRI_DEBIT = "mandiri_debit";
    public static final String BNI_DEBIT_ONLINE = "bni_debit_online";
    @StringDef({
            CIMB,
            BCA,
            MANDIRI,
            BNI,
            MAYBANK,
            BRI,
            DANAMON,
            MANDIRI_DEBIT,
            BNI_DEBIT_ONLINE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface BankDef {
    }
}
