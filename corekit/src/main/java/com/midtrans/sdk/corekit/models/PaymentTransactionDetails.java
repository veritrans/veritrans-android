package com.midtrans.sdk.corekit.models;


import com.midtrans.sdk.corekit.models.snap.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.TransactionDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 2/14/18.
 */

public class PaymentTransactionDetails {
    private TransactionDetails transactionDetails;
    private List<ItemDetails> itemDetailsList;

    public PaymentTransactionDetails(TransactionDetails transactionDetails, List<ItemDetails> itemDetailsList) {
        if (transactionDetails != null) {
            this.transactionDetails = new TransactionDetails(transactionDetails.getOrderId(), transactionDetails.getAmount());
        }
        if (itemDetailsList != null) {
            this.itemDetailsList = new ArrayList<>(itemDetailsList);
        }
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public List<ItemDetails> getItemDetailsList() {
        return itemDetailsList;
    }

    public void setItemDetailsList(List<ItemDetails> itemDetailsList) {
        this.itemDetailsList = itemDetailsList;
    }

    public long getTotalAmount() {
        return transactionDetails != null ? transactionDetails.getAmount() : 0;
    }

    public void setTotalAmount(long totalAmount) {
        if (transactionDetails != null) {
            transactionDetails.setAmount((int) totalAmount);
        }
    }

    public void changePaymentDetails(List<ItemDetails> newItemDetails, long newTotalAmount) {
        if (transactionDetails != null) {
            transactionDetails.setAmount((int) newTotalAmount);
            this.itemDetailsList = newItemDetails;
        }
    }
}
