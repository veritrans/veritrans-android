package com.midtrans.sdk.ui.views.creditcard.points;

import android.util.Log;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;

/**
 * Created by rakawm on 4/5/17.
 */

public class BankPointsPresenter extends BasePaymentPresenter {
    private static final String TAG = BankPointsPresenter.class.getSimpleName();

    private String latestValidPoint;
    private float pointBalance;
    private double totalAmount;
    private double amountToPay;
    private float pointRedeemed;
    private String pointBank;

    public BankPointsPresenter(float pointBalance, String pointBank) {
        initValues();
        setPointBalance(pointBalance);
        setPointBank(pointBank);
    }

    private void initValues() {
        double totalAmount = MidtransUi.getInstance().getTransaction().transactionDetails.grossAmount;
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

    public void setPointBalance(float pointBalance) {
        this.pointBalance = pointBalance;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public float getPointRedeemed() {
        return pointRedeemed;
    }

    public void setPointRedeemed(float pointRedeemed) {
        this.pointRedeemed = pointRedeemed;
    }

    public String getPointBank() {
        return pointBank;
    }

    public void setPointBank(String pointBank) {
        this.pointBank = pointBank;
    }
}
