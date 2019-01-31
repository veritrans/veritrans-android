package com.midtrans.sdk.corekit.base.enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

import static com.midtrans.sdk.corekit.base.enums.ExpiryTimeUnit.DAY;
import static com.midtrans.sdk.corekit.base.enums.ExpiryTimeUnit.HOUR;
import static com.midtrans.sdk.corekit.base.enums.ExpiryTimeUnit.MINUTE;

@StringDef({MINUTE,HOUR,DAY})
@Retention(RetentionPolicy.SOURCE)
public @interface ExpiryTimeUnit {
    String HOUR = "hour";
    String MINUTE = "minute";
    String DAY = "day";
}