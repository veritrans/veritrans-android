package com.midtrans.sdk.uikit.models;

import com.midtrans.sdk.corekit.models.snap.BanksPointResponse;

/**
 * Created by ziahaqi on 1/24/17.
 */
public class CreditCardBankPoint {
    private String bankType;
    private boolean enableStatus;
    private BanksPointResponse bankPoint;
    private float pointRedeemed = 0;

    public void setStatus(boolean bankPointStatus) {
        this.enableStatus = bankPointStatus;
    }

    public void setData(BanksPointResponse bankPoint, String bankType) {
        this.bankPoint = bankPoint;
        this.bankType = bankType;
    }

    public boolean isEnabled() {
        return enableStatus;
    }

    public boolean isValid() {
        return (bankPoint != null && bankType != null && pointRedeemed > 0);
    }

    public float getPointRedeemed() {
        return pointRedeemed;
    }

    public void setPointRedeemed(float pointRedeemed) {
        this.pointRedeemed = pointRedeemed;
    }

    public String getBankType() {
        return bankType;
    }
}
