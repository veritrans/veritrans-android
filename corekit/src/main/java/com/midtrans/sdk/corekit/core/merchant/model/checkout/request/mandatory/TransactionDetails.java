package com.midtrans.sdk.corekit.core.merchant.model.checkout.request.mandatory;

import com.google.gson.annotations.SerializedName;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.utilities.Currency;
import com.midtrans.sdk.corekit.utilities.Logger;

import java.io.Serializable;

import static com.midtrans.sdk.corekit.utilities.StringHelper.checkCurrency;

public class TransactionDetails implements Serializable {
    private String currency;

    @SerializedName("gross_amount")
    private double grossAmount;

    @SerializedName("order_id")
    private String orderId;

    public TransactionDetails(String orderId,
                              double grossAmount,
                              String currency) {
        if (!TextUtils.isEmpty(orderId) && grossAmount > 0) {
            this.orderId = orderId;
            this.grossAmount = grossAmount;
            this.currency = checkCurrency(currency);
        } else {
            Logger.error("Invalid transaction data.");
        }
    }

    public TransactionDetails(String orderId,
                              double grossAmount) {
        if (!TextUtils.isEmpty(orderId) && grossAmount > 0) {
            this.orderId = orderId;
            this.grossAmount = grossAmount;
            this.currency = Currency.IDR;
        } else {
            Logger.error("Invalid transaction data.");
        }
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = checkCurrency(currency);
    }

    public double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(double grossAmount) {
        if (grossAmount > 0) {
            this.grossAmount = grossAmount;
        } else {
            Logger.error("Invalid transaction data, invalid grossAmount.");
        }
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        if (!TextUtils.isEmpty(orderId)) {
            this.orderId = orderId;
        } else {
            Logger.error("Invalid transaction data, empty orderId.");
        }
    }
}