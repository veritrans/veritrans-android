package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.bcaklikpay.BcaKlikPayData;

public class BcaKlikPayResponse extends BasePaymentResponse {

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(String fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public void setBcaKlikPayData(BcaKlikPayData bcaKlikPayData) {
        this.dataResponse = bcaKlikPayData;
    }

    public BcaKlikPayData getBcaKlikPayData() {
        return dataResponse;
    }
}