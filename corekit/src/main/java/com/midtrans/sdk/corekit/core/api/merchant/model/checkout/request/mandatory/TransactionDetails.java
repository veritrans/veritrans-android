package com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.mandatory;

import com.google.gson.annotations.SerializedName;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.enums.Currency;
import com.midtrans.sdk.corekit.utilities.Logger;

import java.io.Serializable;

import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_INVALID_GROSS_TRANSACTION_DATA;
import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_INVALID_ORDER_TRANSACTION_DATA;
import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_INVALID_TRANSACTION_DATA;
import static com.midtrans.sdk.corekit.utilities.StringHelper.checkCurrency;

public class TransactionDetails implements Serializable {
    private String currency;

    @SerializedName("gross_amount")
    private double grossAmount;

    @SerializedName("order_id")
    private String orderId;

    public TransactionDetails(String orderId,
                              double grossAmount,
                              Currency currency) {
        if (!TextUtils.isEmpty(orderId) && grossAmount > 0) {
            this.orderId = orderId;
            this.grossAmount = grossAmount;
            this.currency = checkCurrency(currency);
        } else {
            Logger.error(MESSAGE_INVALID_TRANSACTION_DATA);
        }
    }

    public TransactionDetails(String orderId,
                              double grossAmount) {
        if (!TextUtils.isEmpty(orderId) && grossAmount > 0) {
            this.orderId = orderId;
            this.grossAmount = grossAmount;
            this.currency = checkCurrency(Currency.IDR);
        } else {
            Logger.error(MESSAGE_INVALID_TRANSACTION_DATA);
        }
    }

    public Currency getCurrency() {
        return checkCurrency(currency);
    }

    public void setCurrency(Currency currency) {
        this.currency = checkCurrency(currency);
    }

    public double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(double grossAmount) {
        if (grossAmount > 0) {
            this.grossAmount = grossAmount;
        } else {
            Logger.error(MESSAGE_INVALID_GROSS_TRANSACTION_DATA);
        }
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        if (!TextUtils.isEmpty(orderId)) {
            this.orderId = orderId;
        } else {
            Logger.error(MESSAGE_INVALID_ORDER_TRANSACTION_DATA);
        }
    }
}