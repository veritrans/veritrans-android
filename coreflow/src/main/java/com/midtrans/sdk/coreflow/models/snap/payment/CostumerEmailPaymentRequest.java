package com.midtrans.sdk.coreflow.models.snap.payment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 8/1/16.
 */
public class CostumerEmailPaymentRequest extends BasePaymentRequest{
    @SerializedName("email_address")
    private String emailAddress;
}
