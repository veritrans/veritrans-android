package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author rakawm
 */
public class Transaction {
    private String token;
    @SerializedName("transaction_details")
    private TransactionDetails transactionDetails;
    private Callbacks callbacks;
    @SerializedName("enabled_payments")
    private List<EnabledPayment> enabledPayments;
    @SerializedName("merchant")
    private MerchantData merchantData;
    @SerializedName("credit_card")
    private CreditCard creditCard;
    private List<PromoResponse> promos;

    public Transaction() {
    }

    public MerchantData getMerchantData() {
        return merchantData;
    }

    public void setMerchantData(MerchantData merchantData) {
        this.merchantData = merchantData;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<EnabledPayment> getEnabledPayments() {
        return enabledPayments;
    }

    public void setEnabledPayments(List<EnabledPayment> enabledPayments) {
        this.enabledPayments = enabledPayments;
    }

    public Callbacks getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public List<PromoResponse> getPromos() {
        return promos;
    }

    public void setPromos(List<PromoResponse> promos) {
        this.promos = promos;
    }
}
