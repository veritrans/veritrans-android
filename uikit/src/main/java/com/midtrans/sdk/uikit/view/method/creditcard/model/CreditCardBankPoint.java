package com.midtrans.sdk.uikit.view.method.creditcard.model;

import com.midtrans.sdk.corekit.core.api.snap.model.point.PointResponse;

public class CreditCardBankPoint {
    private String bankType;
    private boolean enableStatus;
    private PointResponse bankPoint;
    private float pointRedeemed = 0;

    public void setStatus(boolean bankPointStatus) {
        this.enableStatus = bankPointStatus;
    }

    public void setData(PointResponse bankPoint, String bankType) {
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