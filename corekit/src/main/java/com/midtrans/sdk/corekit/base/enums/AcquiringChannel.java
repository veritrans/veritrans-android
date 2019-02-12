package com.midtrans.sdk.corekit.base.enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

import static com.midtrans.sdk.corekit.base.enums.AcquiringChannel.MIGS;

@StringDef({MIGS})
@Retention(RetentionPolicy.SOURCE)
public @interface AcquiringChannel {
    String MIGS = "migs";
}
