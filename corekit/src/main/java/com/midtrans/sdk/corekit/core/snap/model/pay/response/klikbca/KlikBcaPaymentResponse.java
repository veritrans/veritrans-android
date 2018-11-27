package com.midtrans.sdk.corekit.core.snap.model.pay.response.klikbca;

import com.google.gson.annotations.SerializedName;

import com.midtrans.sdk.corekit.core.snap.model.pay.response.BasePaymentResponse;

public class KlikBcaPaymentResponse extends BasePaymentResponse {
    @SerializedName("approval_code")
    private String approvalCode;


    @SerializedName("bcak")
    private String approvalCode;

}
