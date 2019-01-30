package com.midtrans.sdk.corekit.models.snap;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.midtrans.sdk.corekit.models.snap.Authentication.*;

/**
 * Created by farhan.pahlevi on 29/01/19.
 */

@StringDef({AUTH_3DS, AUTH_NONE, AUTH_RBA})
@Retention(RetentionPolicy.SOURCE)
public @interface Authentication {
    String AUTH_3DS = "3ds";
    String AUTH_NONE = "none";
    String AUTH_RBA = "rba";
}