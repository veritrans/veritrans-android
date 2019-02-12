package com.midtrans.sdk.uikit.base.enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

import static com.midtrans.sdk.uikit.base.enums.PaymentStatus.STATUS_FAILED;
import static com.midtrans.sdk.uikit.base.enums.PaymentStatus.STATUS_INVALID;
import static com.midtrans.sdk.uikit.base.enums.PaymentStatus.STATUS_PENDING;
import static com.midtrans.sdk.uikit.base.enums.PaymentStatus.STATUS_SUCCESS;

@StringDef({STATUS_SUCCESS, STATUS_PENDING, STATUS_INVALID, STATUS_FAILED})
@Retention(RetentionPolicy.SOURCE)
public @interface PaymentStatus {
    String STATUS_SUCCESS = "success";
    String STATUS_PENDING = "pending";
    String STATUS_INVALID = "invalid";
    String STATUS_FAILED = "failed";
}