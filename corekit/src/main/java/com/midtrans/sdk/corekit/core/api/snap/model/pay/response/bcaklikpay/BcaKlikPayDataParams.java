package com.midtrans.sdk.corekit.core.api.snap.model.pay.response.bcaklikpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BcaKlikPayDataParams {
    @SerializedName("klikPayCode")
    @Expose
    private String klikPayCode;
    @SerializedName("transactionNo")
    @Expose
    private String transactionNo;
    @SerializedName("totalAmount")
    @Expose
    private String totalAmount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("payType")
    @Expose
    private String payType;
    @SerializedName("callback")
    @Expose
    private String callback;
    @SerializedName("transactionDate")
    @Expose
    private String transactionDate;
    @SerializedName("descp")
    @Expose
    private String descp;
    @SerializedName("miscFee")
    @Expose
    private String miscFee;
    @SerializedName("signature")
    @Expose
    private String signature;

    public String getKlikPayCode() {
        return klikPayCode;
    }

    public void setKlikPayCode(String klikPayCode) {
        this.klikPayCode = klikPayCode;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getMiscFee() {
        return miscFee;
    }

    public void setMiscFee(String miscFee) {
        this.miscFee = miscFee;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}