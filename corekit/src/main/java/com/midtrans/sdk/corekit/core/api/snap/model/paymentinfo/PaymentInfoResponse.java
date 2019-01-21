package com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo;

import com.google.gson.annotations.SerializedName;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.mandatory.TransactionDetails;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.GopayDeepLink;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.Item;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.CustomerDetails;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard.CreditCard;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.callback.Callbacks;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.enablepayment.EnabledPayment;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.merchantdata.MerchantData;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.promo.PromoDetails;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.promo.PromoResponse;

import java.io.Serializable;
import java.util.List;

public class PaymentInfoResponse implements Serializable {
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
    private List<Item> itemDetails;

    @SerializedName("customer_details")
    private CustomerDetails customerDetails;

    @SerializedName("gopay")
    private GopayDeepLink gopay;

    public PaymentInfoResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public Callbacks getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public List<EnabledPayment> getEnabledPayments() {
        return enabledPayments;
    }

    public void setEnabledPayments(List<EnabledPayment> enabledPayments) {
        this.enabledPayments = enabledPayments;
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

    public List<Item> getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(List<Item> itemDetails) {
        this.itemDetails = itemDetails;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    public GopayDeepLink getGopay() {
        return gopay;
    }

    public void setGopay(GopayDeepLink gopay) {
        this.gopay = gopay;
    }
}