package com.midtrans.sdk.corekit.models.snap;

import java.util.List;

/**
 * @author rakawm
 */
@Deprecated
public class TransactionData {
    private String id;
    private String transactionId;
    private String kind;
    private TransactionDetails transactionDetails;
    private List<String> enabledPayments;
    private BankTransfer bankTransfer;

    private CustomerDetails customerDetails;
    private List<ItemDetails> itemDetails;
    private PaymentOptions paymentOptions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public List<String> getEnabledPayments() {
        return enabledPayments;
    }

    public void setEnabledPayments(List<String> enabledPayments) {
        this.enabledPayments = enabledPayments;
    }

    public BankTransfer getBankTransfer() {
        return bankTransfer;
    }

    public void setBankTransfer(BankTransfer bankTransfer) {
        this.bankTransfer = bankTransfer;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    public List<ItemDetails> getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(List<ItemDetails> itemDetails) {
        this.itemDetails = itemDetails;
    }

    public PaymentOptions getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(PaymentOptions paymentOptions) {
        this.paymentOptions = paymentOptions;
    }
}
