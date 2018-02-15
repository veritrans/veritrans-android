package com.midtrans.sdk.uikit.views.creditcard.bankpoints;

import android.text.TextUtils;
import android.util.Log;

import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.PaymentTransactionDetails;
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
    private String latestValidPoint;
    private String itemDetailsName;

    public BankPointsPresenter(float pointBalance, String pointBank) {
        super();
        initValues();
        this.pointBalance = pointBalance;
        this.pointBank = pointBank;
    }

    private void initValues() {
        double totalAmount = 0;

        PaymentTransactionDetails paymentDetails = getMidtransSDK().getPaymentTransactionDetails();
        if (paymentDetails != null) {
            totalAmount = paymentDetails.getTotalAmount();
        }
        this.amountToPay = totalAmount;
        this.totalAmount = totalAmount;
        this.pointRedeemed = 0;
    }

    public boolean isValidInputPoint(String inputString) {
        long currentBalance = 0;
        try {
            currentBalance = Long.parseLong(inputString);
        } catch (RuntimeException e) {
            Log.e(TAG, "currentBalance:" + e.getMessage());
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
}
