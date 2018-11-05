package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.promo.PromoDetails;

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

    @Deprecated
    private List<PromoResponse> promos;

    @SerializedName("promo_details")
    private PromoDetails promoDetails;

    @SerializedName("item_details")
    private List<ItemDetails> itemDetails;

    @SerializedName("customer_details")
    private CustomerDetails customerDetails;

    @SerializedName("gopay")
    private Gopay gopay;

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

    public PromoDetails getPromoDetails() {
        return promoDetails;
    }

    public void setPromoDetails(PromoDetails promoDetails) {
        this.promoDetails = promoDetails;
    }

    public List<ItemDetails> getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(List<ItemDetails> itemDetails) {
        this.itemDetails = itemDetails;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    public Gopay getGopay() {
        return gopay;
    }

    public void setGopay(Gopay gopay) {
        this.gopay = gopay;
    }
}
