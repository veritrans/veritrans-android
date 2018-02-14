package com.midtrans.sdk.corekit.models;


import com.midtrans.sdk.corekit.models.snap.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.TransactionDetails;

import java.util.List;

/**
 * Created by ziahaqi on 2/14/18.
 */

public class PaymentTransactionDetails {
    private TransactionDetails transactionDetails;
    private List<ItemDetails> itemDetailsList;

    public PaymentTransactionDetails(TransactionDetails transactionDetails, List<ItemDetails> itemDetailsList) {
        this.transactionDetails = transactionDetails;
        this.itemDetailsList = itemDetailsList;
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
}
