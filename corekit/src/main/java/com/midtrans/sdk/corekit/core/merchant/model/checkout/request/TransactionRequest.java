package com.midtrans.sdk.corekit.core.merchant.model.checkout.request;

import com.google.gson.annotations.SerializedName;

import android.support.annotation.NonNull;

import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.ItemDetails;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.mandatory.TransactionDetails;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.customer.CustomerDetails;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.BillInfoModel;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.ExpiryModel;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.GopayDeeplink;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.SnapPromo;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.specific.banktransfer.BankTransferRequestModel;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.specific.banktransfer.BcaBankTransferRequestModel;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.specific.creditcard.CreditCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionRequest implements Serializable {

    /**
     * contains details about transaction.
     * Mandatory
     */
    @SerializedName("transaction_details")
    private TransactionDetails transactionDetails = null;

    /**
     * contains details about customer.
     * Mandatory
     */
    @SerializedName("customer_details")
    private CustomerDetails customerDetails = null;

    /**
     * List of purchased items.
     * Mandatory
     */
    @SerializedName("item_details")
    private ArrayList<ItemDetails> itemDetails = new ArrayList<>();

    /**
     * List of enable payment.
     * Optional
     */
    @SerializedName("enabled_payments")
    private List<String> enabledPayments = new ArrayList<>();

    /**
     * Contains user app deeplink for merchant app.
     * Optional
     */
    @SerializedName("gopay")
    private GopayDeeplink gopay = null;

    /**
     * Set custom expiry of token that will be created
     */
    @SerializedName("expiry")
    private ExpiryModel expiry = null;

    /**
     * Contain maximum 3 of custom field for user.
     * Optional
     */
    @SerializedName("custom_field1")
    private String customField1 = null;
    @SerializedName("custom_field2")
    private String customField2 = null;
    @SerializedName("custom_field3")
    private String customField3 = null;

    /**
     * It contains an extra information that you want to display on bill.
     * Optional
     */
    private BillInfoModel billInfoModel = null;

    /**
     * It's user id.
     * Optional
     */
    @SerializedName("user_id")
    private String userId = null;

    /**
     * It contains an promo information.
     * Optional
     */
    private SnapPromo promo = null;

    /**
     * Contain Permata VA details.
     * Specific for bank transfer Permata VA
     */
    @SerializedName("permata_va")
    private BankTransferRequestModel permataVa = null;

    /**
     * Contain BCA VA details.
     * Specific for bank transfer BCA VA
     */
    @SerializedName("bca_va")
    private BcaBankTransferRequestModel bcaVa = null;

    /**
     * Contain BNI VA details.
     * Specific for bank transfer BNI VA
     */
    @SerializedName("bni_va")
    private BankTransferRequestModel bniVa = null;

    /**
     * Contain credit card details.
     * Specific for credit card
     */
    @SerializedName("credit_card")
    private CreditCard creditCard = null;

    /**
     * helps to identify whether to use ui or not.
     */
    private Map<String, String> customObject = null;


    /**
     * TransactionRequest constructor for mandatory item
     *
     * @param transactionDetails contains order id, currency, and gross amount of transaction.
     */
    public TransactionRequest(@NonNull TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    /**
     * TransactionRequest constructor for mandatory item without using TransactionDetails object
     * but the request use IDR, if want to custom use SGD, use setter or TransactionRequest constructor with object¬
     *
     * @param orderId     contains order id.
     * @param grossAmount contains gross amount.
     */
    public TransactionRequest(@NonNull String orderId,
                              @NonNull double grossAmount) {
        this.transactionDetails = new TransactionDetails(orderId, grossAmount);
    }

    /**
     * Start getter and setter for mandatory object
     */
    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public ArrayList<ItemDetails> getItemDetails() {
        return itemDetails;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    public void setItemDetails(ArrayList<ItemDetails> itemDetails) {
        this.itemDetails = itemDetails;
    }

    /**
     * Start getter and setter for optional object
     */
    public List<String> getEnabledPayments() {
        return enabledPayments;
    }

    public void setEnabledPayments(List<String> enabledPayments) {
        this.enabledPayments = enabledPayments;
    }

    public GopayDeeplink getGopay() {
        return gopay;
    }

    public void setGopay(GopayDeeplink gopay) {
        this.gopay = gopay;
    }

    public ExpiryModel getExpiry() {
        return expiry;
    }

    public void setExpiry(ExpiryModel expiry) {
        this.expiry = expiry;
    }

    public String getCustomField1() {
        return customField1;
    }

    public void setCustomField1(String customField1) {
        this.customField1 = customField1;
    }

    public String getCustomField2() {
        return customField2;
    }

    public void setCustomField2(String customField2) {
        this.customField2 = customField2;
    }

    public String getCustomField3() {
        return customField3;
    }

    public void setCustomField3(String customField3) {
        this.customField3 = customField3;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Start getter and setter for specific object
     */
    public SnapPromo getPromo() {
        return promo;
    }

    public void setPromo(SnapPromo promo) {
        this.promo = promo;
    }

    public BankTransferRequestModel getPermataVa() {
        return permataVa;
    }

    public void setPermataVa(BankTransferRequestModel permataVa) {
        this.permataVa = permataVa;
    }

    public BcaBankTransferRequestModel getBcaVa() {
        return bcaVa;
    }

    public void setBcaVa(BcaBankTransferRequestModel bcaVa) {
        this.bcaVa = bcaVa;
    }

    public BankTransferRequestModel getBniVa() {
        return bniVa;
    }

    public void setBniVa(BankTransferRequestModel bniVa) {
        this.bniVa = bniVa;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
}