package com.midtrans.sdk.uikit.models;

import com.midtrans.sdk.corekit.models.snap.BankPointsResponse;

/**
 * Created by ziahaqi on 1/24/17.
 */
public class CreditCardBankPoint {
    private String bankType;
    private boolean enableStatus;
    private BankPointsResponse bankPoint;
    private long pointRedeemed = 0;

    public void setStatus(boolean bankPointStatus) {
        this.enableStatus = bankPointStatus;
    }

    public void setData(BankPointsResponse bankPoint, String bankType) {
        this.bankPoint = bankPoint;
        this.bankType = bankType;
    }

    public boolean isEnabled() {
        return enableStatus;
    }

    public void setpointRedeemed(long pointRedeemed) {
        this.pointRedeemed = pointRedeemed;
    }

    public boolean isValid() {
        return (bankPoint != null && bankType != null && pointRedeemed > 0);
    }

    public long getpointRedeemed() {
        return pointRedeemed;
    }
}
