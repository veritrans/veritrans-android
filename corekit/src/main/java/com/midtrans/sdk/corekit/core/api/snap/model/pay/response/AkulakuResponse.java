package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

public class AkulakuResponse extends BasePaymentResponse {

    public String getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(String fraudStatus) {
        this.fraudStatus = fraudStatus;
    }
}