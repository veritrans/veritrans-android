package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.midtrans.sdk.corekit.base.enums.Currency;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.bcaklikpay.BcaKlikPayData;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.va.VaNumber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.midtrans.sdk.corekit.utilities.StringHelper.checkCurrency;

public class BasePaymentResponse implements Serializable {

    @SerializedName("status_code")
    @Expose
    protected String statusCode;
    @SerializedName("status_message")
    @Expose
    protected String statusMessage;
    @SerializedName("redirect_url")
    @Expose
    protected String redirectUrl;
    @SerializedName("transaction_id")
    @Expose
    protected String transactionId;
    @SerializedName("order_id")
    @Expose
    protected String orderId;
    @SerializedName("gross_amount")
    @Expose
    protected String grossAmount;
    @SerializedName("currency")
    @Expose
    protected String currency;
    @SerializedName("payment_type")
    @Expose
    protected String paymentType;
    @SerializedName("transaction_time")
    @Expose
    protected String transactionTime;
    @SerializedName("transaction_status")
    @Expose
    protected String transactionStatus;
    @SerializedName("finish_redirect_url")
    @Expose
    protected String finishRedirectUrl;
    @SerializedName("settlement_time")
    @Expose
    protected String settlementTime;

    /**
     * KlikBCA
     */
    @SerializedName("approval_code")
    @Expose
    protected String approvalCode;

    /**
     * BcaKlikPay
     */
    @SerializedName("redirect_data")
    @Expose
    protected BcaKlikPayData dataResponse;

    /**
     * BRIEpay
     */
    @SerializedName("fraud_status")
    @Expose
    protected String fraudStatus;

    /**
     * Base Bank Transfer Va
     */
    @SerializedName("type")
    @Expose
    protected String type;
    @SerializedName("pdf_url")
    @Expose
    protected String pdfUrl;
    @SerializedName("atm_channel")
    @Expose
    protected String atmChannel;

    /**
     * Bca Bank Transfer Va
     */
    @SerializedName("bca_va_number")
    @Expose
    protected String bcaVaNumber;
    @SerializedName("bca_expiration")
    @Expose
    protected String bcaExpiration;
    @SerializedName("va_numbers")
    @Expose
    protected List<VaNumber> vaNumbersList;

    /**
     * BNI Bank Transfer Va
     */
    @SerializedName("bni_va_number")
    @Expose
    protected String bniVaNumber;
    @SerializedName("bni_expiration")
    @Expose
    protected String bniExpiration;

    /**
     * Permata Bank Tranfer Va
     */
    @SerializedName("permata_va_number")
    @Expose
    protected String permataVaNumber;
    @SerializedName("permata_expiration")
    @Expose
    protected String permataExpiration;
    @SerializedName("user_id")
    protected String userId;

    /**
     * Echannel
     */
    @SerializedName("bill_key")
    protected String billKey;
    @SerializedName("biller_code")
    protected String billerCode;
    @SerializedName("billpayment_expiration")
    protected String billPaymentExpiration;

    /**
     * get bank point
     */
    @SerializedName("validation_messages")
    protected ArrayList<String> validationMessages;
    @SerializedName("point_balance")
    protected Long pointBalance;
    @SerializedName("point_balance_amount")
    protected String pointBalanceAmount;

    /**
     * get gopay
     */
    @SerializedName("qr_code_url")
    protected String qrCodeUrl;
    @SerializedName("deeplink_url")
    protected String deeplinkUrl;
    @SerializedName("gopay_expiration")
    protected String gopayExpiration;
    @SerializedName("gopay_expiration_raw")
    protected String gopayExpirationRaw;

    /**
     * Indomaret
     */
    @SerializedName("payment_code")
    protected String paymentCode;
    @SerializedName("store")
    protected String store;
    @SerializedName("indomaret_expire_time")
    protected String indomaretExpireTime;

    /**
     * Klik Bca
     */
    @SerializedName("bca_klikbca_expire_time")
    protected String klikBcaExpireTime;

    /**
     * Mandiri Clickpay
     */
    @SerializedName("masked_card")
    protected String maskedCard;

    /**
     * CreditCard
     */
    @SerializedName("bank")
    protected String bank;
    @SerializedName("card_type")
    protected String cardType;

    public void setValidationMessages(ArrayList<String> validationMessages) {
        this.validationMessages = validationMessages;
    }

    public ArrayList<String> getValidationMessages() {
        return validationMessages;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = checkCurrency(currency);
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public String getFinishRedirectUrl() {
        return finishRedirectUrl;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public void setFinishRedirectUrl(String finishRedirectUrl) {
        this.finishRedirectUrl = finishRedirectUrl;
    }

}