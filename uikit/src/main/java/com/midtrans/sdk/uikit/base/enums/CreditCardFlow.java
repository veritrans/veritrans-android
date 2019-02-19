package com.midtrans.sdk.uikit.base.enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

import static com.midtrans.sdk.uikit.base.enums.CreditCardFlow.NORMAL;
import static com.midtrans.sdk.uikit.base.enums.CreditCardFlow.ONE_CLICK;
import static com.midtrans.sdk.uikit.base.enums.CreditCardFlow.TWO_CLICK;

@StringDef({ONE_CLICK, TWO_CLICK, NORMAL})
@Retention(RetentionPolicy.SOURCE)
public @interface CreditCardFlow {
    String ONE_CLICK = "one_click";
    String TWO_CLICK = "two_click";
    String NORMAL = "normal";
}