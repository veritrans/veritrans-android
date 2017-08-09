package com.midtrans.sdk.uikit.views.creditcard.bankpoints;

import android.util.Log;

import com.midtrans.sdk.corekit.core.MidtransSDK;

/**
 * Created by ziahaqi on 7/25/17.
 */

public class BankPointsPresenter {
    private static final String TAG = BankPointsPresenter.class.getSimpleName();
    private String pointBank;
    private float pointBalance;
    private final BankPointsView view;
    private double totalAmount;
    private double amountToPay;
    private float pointRedeemed;
    private String latestValidPoint;

    public BankPointsPresenter(BankPointsView view, float pointBalance, String pointBank) {
        initValues();
        this.view = view;
        this.pointBalance = pointBalance;
        this.pointBank = pointBank;
    }

    private void initValues() {
        double totalAmount = MidtransSDK.getInstance().getTransaction().getTransactionDetails().getAmount();
        this.amountToPay = totalAmount;
        this.totalAmount = totalAmount;
        this.pointRedeemed = 0;
    }

    public boolean isValidInputPoint(String inputString) {
        long currentBalance = 0;
        try {
            currentBalance = Long.parseLong(inputString);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        if (currentBalance >= 0 && currentBalance <= pointBalance) {
            calculateAmount(currentBalance);
            return true;
        }
        return false;
    }

    public void calculateAmount(float currentBalance) {
        this.pointRedeemed = currentBalance;
        this.amountToPay = totalAmount - pointRedeemed;
    }

    public String getLatestValidPoint() {
        return latestValidPoint;
    }

    public void setLatestValidPoint(String latestValidPoint) {
        this.latestValidPoint = latestValidPoint;
    }

    public float getPointBalance() {
        return pointBalance;
    }

    public double getAmountToPay() {
        return amountToPay;
    }

    public String getPointBank() {
        return pointBank;
    }

    public String getSemiBoldFontPath() {
        return MidtransSDK.getInstance().getSemiBoldText();
    }
}
