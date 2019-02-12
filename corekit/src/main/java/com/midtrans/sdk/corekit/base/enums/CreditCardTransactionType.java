package com.midtrans.sdk.corekit.base.enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

import static com.midtrans.sdk.corekit.base.enums.CreditCardTransactionType.AUTHORIZE;
import static com.midtrans.sdk.corekit.base.enums.CreditCardTransactionType.AUTHORIZE_CAPTURE;

@StringDef({
        AUTHORIZE,
        AUTHORIZE_CAPTURE
})
@Retention(RetentionPolicy.SOURCE)
public @interface CreditCardTransactionType {
    String AUTHORIZE = "authorize";
    String AUTHORIZE_CAPTURE = "authorize_capture";
}