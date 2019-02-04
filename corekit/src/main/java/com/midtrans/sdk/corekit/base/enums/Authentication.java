package com.midtrans.sdk.corekit.base.enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

import static com.midtrans.sdk.corekit.base.enums.Authentication.AUTH_3DS;
import static com.midtrans.sdk.corekit.base.enums.Authentication.AUTH_NONE;
import static com.midtrans.sdk.corekit.base.enums.Authentication.AUTH_RBA;

@StringDef({
        AUTH_3DS,
        AUTH_RBA,
        AUTH_NONE
})
@Retention(RetentionPolicy.SOURCE)
public @interface Authentication {
    String AUTH_3DS = "3ds";
    String AUTH_RBA = "rba";
    String AUTH_NONE = "none";
}