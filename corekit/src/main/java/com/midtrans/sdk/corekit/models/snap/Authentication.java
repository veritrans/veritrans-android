package com.midtrans.sdk.corekit.models.snap;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by farhan.pahlevi on 29/01/19.
 */

public class Authentication {

    public static final String AUTH_3DS = "3ds";
    public static final String AUTH_NONE = "none";
    public static final String AUTH_RBA = "rba";

    @StringDef({AUTH_3DS, AUTH_NONE, AUTH_RBA})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CreditCardAuth {
    }
}