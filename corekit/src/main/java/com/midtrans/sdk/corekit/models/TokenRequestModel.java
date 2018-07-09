package com.midtrans.sdk.corekit.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import com.midtrans.sdk.corekit.models.snap.BankTransferRequestModel;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.SnapPromo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 7/19/16.
 */
public class TokenRequestModel {

    @SerializedName("customer_details")
    private final CustomerDetails costumerDetails;

    @SerializedName("item_details")
    private ArrayList<ItemDetails> itemDetails;

    @SerializedName("transaction_details")
    private SnapTransactionDetails transactionDetails;

    @SerializedName("credit_card")
    private CreditCard creditCard;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("permata_va")
    private BankTransferRequestModel permataVa;

    @SerializedName("bca_va")
    private BcaBankTransferRequestModel bcaVa;

    @SerializedName("bni_va")
    private BankTransferRequestModel bniVa;

    @SerializedName("enabled_payments")
    private List<String> enabledPayments;

    private ExpiryModel expiry;
    private SnapPromo promo;
    // Custom field
    @SerializedName("custom_field1")
    private String customField1;
    @SerializedName("custom_field2")
    private String customField2;
    @SerializedName("custom_field3")
    private String customField3;

    public TokenRequestModel(SnapTransactionDetails transactionDetails, ArrayList<ItemDetails> itemDetails,
                             CustomerDetails customerDetails) {
        this.transactionDetails = transactionDetails;
        this.itemDetails = itemDetails;
        this.costumerDetails = customerDetails;
    }

    public TokenRequestModel(SnapTransactionDetails transactionDetails, ArrayList<ItemDetails> itemDetails, CustomerDetails customerDetails, CreditCard creditCard) {
        this.transactionDetails = transactionDetails;
        this.itemDetails = itemDetails;
        this.costumerDetails = customerDetails;
        this.creditCard = creditCard;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public CustomerDetails getCostumerDetails() {
        return costumerDetails;
    }

    public ArrayList<ItemDetails> getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(ArrayList<ItemDetails> itemDetails) {
        this.itemDetails = itemDetails;
    }

    public SnapTransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(SnapTransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public String getString() {
        String json = "";
        try {
            Gson gson = new Gson();
            json = gson.toJson(this);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return json;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ExpiryModel getExpiry() {
        return expiry;
    }

    public void setExpiry(ExpiryModel expiry) {
        this.expiry = expiry;
    }

    public SnapPromo getPromo() {
        return promo;
    }

    public void setPromo(SnapPromo promo) {
        this.promo = promo;
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

    public BankTransferRequestModel getPermataVa() {
        return permataVa;
    }

    public void setPermataVa(BankTransferRequestModel permataVa) {
        this.permataVa = permataVa;
    }

    public BankTransferRequestModel getBcaVa() {
        return bcaVa;
    }

    public BankTransferRequestModel getBniVa() {
        return bniVa;
    }

    public void setBcaVa(BcaBankTransferRequestModel bcaVa) {
        this.bcaVa = bcaVa;
    }

    public void setBcaVa(BankTransferRequestModel bniVa) {
        this.bniVa = bniVa;
    }

    public List<String> getEnabledPayments() {
        return enabledPayments;
    }

    public void setEnabledPayments(List<String> enabledPayments) {
        this.enabledPayments = enabledPayments;
    }
}
