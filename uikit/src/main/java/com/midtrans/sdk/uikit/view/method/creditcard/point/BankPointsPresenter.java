package com.midtrans.sdk.uikit.view.method.creditcard.point;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.enums.BankType;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.Item;
import com.midtrans.sdk.uikit.base.composer.BasePaymentPresenter;
import com.midtrans.sdk.uikit.utilities.Constants;

public class BankPointsPresenter extends BasePaymentPresenter {
    private String pointBank;
    private float pointBalance;
    private double totalAmount;
    private double amountToPay;
    private float pointRedeemed;
    private long latestValidPoint;
    private String itemDetailsName;

    public BankPointsPresenter(float pointBalance, String pointBank) {
        super();
        initValues();
        this.pointBalance = pointBalance;
        this.pointBank = pointBank;
    }

    private void initValues() {
        double totalAmount = 0;

        if (getMidtransSdk().getCheckoutTransaction() != null) {
            totalAmount = getMidtransSdk().getCheckoutTransaction().getTransactionDetails().getGrossAmount();
        }
        this.amountToPay = totalAmount;
        this.totalAmount = totalAmount;
        this.pointRedeemed = 0;
    }

    public boolean isValidInputPoint(float pointInputted) {
        return pointInputted >= 0 && pointInputted <= pointBalance;
    }

    public void calculateAmount(float currentBalance) {
        this.pointRedeemed = currentBalance;
        this.amountToPay = totalAmount - pointRedeemed;
    }

    public long getLatestValidPoint() {
        return latestValidPoint;
    }

    public void setLatestValidPoint(long latestValidPoint) {
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

    public Item createBankPointItemDetails() {
        Item newItemDetails = null;

        if (!TextUtils.isEmpty(pointBank) && !TextUtils.isEmpty(itemDetailsName)) {

            int newPrice = (int) pointRedeemed * -1;
            int quantity = 1;

            switch (pointBank) {
                case BankType.BNI:
                    newItemDetails = new Item(Constants.BNI_POINT_ID, newPrice, quantity, itemDetailsName);
                    break;

                case BankType.MANDIRI:
                    newItemDetails = new Item(Constants.MANDIRI_POIN_ID, newPrice, quantity, itemDetailsName);
                    break;
            }
        }

        return newItemDetails;
    }


    public void setItemDetailsName(String itemDetailsName) {
        this.itemDetailsName = itemDetailsName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}