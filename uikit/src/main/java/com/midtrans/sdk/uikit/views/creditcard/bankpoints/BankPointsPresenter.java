package com.midtrans.sdk.uikit.views.creditcard.bankpoints;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.TransactionDetails;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;

/**
 * Created by ziahaqi on 7/25/17.
 */

public class BankPointsPresenter extends BasePaymentPresenter {
    private static final String BNI_POINT_ID = "bni_point";
    private static final String MANDIRI_POIN_ID = "mandiri_point";
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

        TransactionDetails transactionDetails = getMidtransSDK().getTransaction().getTransactionDetails();
        if (transactionDetails != null) {
            totalAmount = transactionDetails.getAmount();
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

    public ItemDetails createBankPointItemDetails() {
        ItemDetails newItemDetails = null;

        if (!TextUtils.isEmpty(pointBank) && !TextUtils.isEmpty(itemDetailsName)) {

            int newPrice = (int) pointRedeemed * -1;
            int quantity = 1;

            switch (pointBank) {
                case BankType.BNI:
                    newItemDetails = new ItemDetails(BNI_POINT_ID, newPrice, quantity, itemDetailsName);
                    break;

                case BankType.MANDIRI:
                    newItemDetails = new ItemDetails(MANDIRI_POIN_ID, newPrice, quantity, itemDetailsName);
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
