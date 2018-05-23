package com.midtrans.sdk.uikit.views.creditcard.bankpoints;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.PaymentDetails;
import com.midtrans.sdk.corekit.models.snap.ItemDetails;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

/**
 * Created by ziahaqi on 7/25/17.
 */

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

        PaymentDetails paymentDetails = getMidtransSDK().getPaymentDetails();
        if (paymentDetails != null) {
            totalAmount = paymentDetails.getTotalAmount();
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
                    newItemDetails = new ItemDetails(UiKitConstants.BNI_POINT_ID, itemDetailsName, newPrice, quantity);
                    break;

                case BankType.MANDIRI:
                    newItemDetails = new ItemDetails(UiKitConstants.MANDIRI_POIN_ID, itemDetailsName, newPrice, quantity);
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
