package com.midtrans.sdk.uikit.base.enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

import static com.midtrans.sdk.uikit.base.enums.CreditCardType.TYPE_MASTER_VISA;
import static com.midtrans.sdk.uikit.base.enums.CreditCardType.TYPE_MASTER_VISA_AMEX;
import static com.midtrans.sdk.uikit.base.enums.CreditCardType.TYPE_MASTER_VISA_JCB;
import static com.midtrans.sdk.uikit.base.enums.CreditCardType.TYPE_MASTER_VISA_JCB_AMEX;
import static com.midtrans.sdk.uikit.base.enums.CreditCardType.TYPE_UNKNOWN;


@IntDef({TYPE_MASTER_VISA_JCB_AMEX, TYPE_MASTER_VISA, TYPE_MASTER_VISA_JCB, TYPE_MASTER_VISA_AMEX, TYPE_UNKNOWN})
@Retention(RetentionPolicy.SOURCE)
public @interface CreditCardType {
    int TYPE_MASTER_VISA_JCB_AMEX = 1;
    int TYPE_MASTER_VISA = 2;
    int TYPE_MASTER_VISA_JCB = 3;
    int TYPE_MASTER_VISA_AMEX = 4;
    int TYPE_UNKNOWN = 0;
}