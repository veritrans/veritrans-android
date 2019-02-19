package com.midtrans.sdk.uikit.base.enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

import static com.midtrans.sdk.uikit.base.enums.CreditCardIssuer.AMEX;
import static com.midtrans.sdk.uikit.base.enums.CreditCardIssuer.JCB;
import static com.midtrans.sdk.uikit.base.enums.CreditCardIssuer.MASTERCARD;
import static com.midtrans.sdk.uikit.base.enums.CreditCardIssuer.VISA;

@StringDef({VISA, MASTERCARD, JCB, AMEX})
@Retention(RetentionPolicy.SOURCE)
public @interface CreditCardIssuer {
    String VISA = "visa";
    String MASTERCARD = "mastercard";
    String JCB = "jcb";
    String AMEX = "amex";
}