package com.midtrans.sdk.corekit.base.enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

import static com.midtrans.sdk.corekit.base.enums.Currency.IDR;
import static com.midtrans.sdk.corekit.base.enums.Currency.SGD;

@StringDef({IDR, SGD})
@Retention(RetentionPolicy.SOURCE)
public @interface Currency {
    String IDR = "idr";
    String SGD = "sgd";
}