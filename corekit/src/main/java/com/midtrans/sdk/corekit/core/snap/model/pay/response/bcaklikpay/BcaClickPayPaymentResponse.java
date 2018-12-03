package com.midtrans.sdk.corekit.core.snap.model.pay.response.bcaklikpay;

import com.google.gson.annotations.SerializedName;

import com.midtrans.sdk.corekit.core.snap.model.pay.response.BasePaymentResponse;

public class BcaClickPayPaymentResponse extends BasePaymentResponse {

    @SerializedName("redirect_data")
    BcaKlikPayDataResponse dataResponse;

    public BcaKlikPayDataResponse getDataResponse() {
        return dataResponse;
    }

    public void setDataResponse(BcaKlikPayDataResponse dataResponse) {
        this.dataResponse = dataResponse;
    }
}