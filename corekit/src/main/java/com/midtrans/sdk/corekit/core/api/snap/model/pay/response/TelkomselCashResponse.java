package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

public class TelkomselCashResponse extends BasePaymentResponse {

    public String getSettlementTime() {
        return settlementTime;
    }

    public void setSettlementTime(String settlementStatus) {
        this.settlementTime = settlementStatus;
    }

}