package com.midtrans.sdk.corekit.models;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * contains information about payment charge api call like, </p> status message, status code,
 * transaction id, transaction status etc.
 * <p>
 * Created by chetan on 30/10/15.
 */
public class TransactionResponse implements Serializable {
    /**
     * status_code : 200 status_message : Success, Credit Card 3D Secure transaction is successful
     * transaction_id : 49ab48d4-93e1-4b52-a706-2f7a746b99d0 saved_token_id :
     * 48111119d4a368-602b-4352-a1ac-23bad256741d masked_card : 481111-1114 order_id : 109380dv0
     * gross_amount : 10000.00 payment_type : credit_card transaction_time : 2015-10-30 19:57:33
     * transaction_status : capture fraud_status : accept saved_token_id_expired_at : 2025-10-30
     * 19:57:36 approval_code : 1446209855742 secure_token : true bank : bni eci : 05
     */

    @SerializedName("status_code")
    private String statusCode;
    @SerializedName("status_message")
    private String statusMessage;
    @SerializedName("transaction_id")
    private String transactionId;
    @SerializedName("saved_token_id")
    private String savedTokenId;
    @SerializedName("masked_card")
    private String maskedCard;
    @SerializedName("three_ds_version")
    private String threeDsVersion;

    @SerializedName("order_id")
    private String orderId;
    @SerializedName("gross_amount")
    private String grossAmount;
    @SerializedName("payment_type")
    private String paymentType;
    @SerializedName("transaction_time")
    private String transactionTime;
    @SerializedName("transaction_status")
    private String transactionStatus;

    @SerializedName("fraud_status")
    private String fraudStatus;
    @SerializedName("saved_token_id_expired_at")
    private String savedTokenIdExpiredAt;
    @SerializedName("approval_code")
    private String approvalCode;
    @SerializedName("secure_token")
    private boolean secureToken;
    @SerializedName("permata_va_number")
    private String permataVANumber;
    @SerializedName("permata_expiration")
    private String permataExpiration;
    @SerializedName("va_numbers")
    private List<VaNumber> accountNumbers;
    @SerializedName("bca_klikbca_expire_time")
    private String bcaKlikBcaExpiration;
    @SerializedName("bca_va_number")
    private String bcaVaNumber;
    @SerializedName("bca_expiration")
    private String bcaExpiration;
    @SerializedName("bni_va_number")
    private String bniVaNumber;
    @SerializedName("bni_expiration")
    private String bniExpiration;
    @SerializedName("bri_va_number")
    private String briVaNumber;
    @SerializedName("bri_expiration")
    private String briExpiration;
    @SerializedName("billpayment_expiration")
    private String mandiriBillExpiration;
    @SerializedName("xl_tunai_order_id")
    private String xlTunaiOrderId;
    @SerializedName("xl_tunai_merchant_id")
    private String xlTunaiMerchantId;
    @SerializedName("xl_expiration")
    private String xlTunaiExpiration;
    @SerializedName("installment_term")
    private String installmentTerm;
    @SerializedName("gopay_expiration")
    private String gopayExpiration;
    @SerializedName("alfamart_expire_time")
    private String alfamartExpireTime;

    @SerializedName("gopay_expiration_raw")
    private String gopayExpirationRaw;

    @SerializedName("indomaret_expire_time")
    private String indomaretExpireTime;

    @SerializedName("redirect_url")
    private String redirectUrl;

    @SerializedName("uob_ezpay_web_url")
    private String uobWebUrl;

    public String getUobWebUrl() {
        return uobWebUrl;
    }

    public void setUobWebUrl(String uobWebUrl) {
        this.uobWebUrl = uobWebUrl;
    }

    public String getUobDeeplinkUrl() {
        return uobDeeplinkUrl;
    }

    public void setUobDeeplinkUrl(String uobDeeplinkUrl) {
        this.uobDeeplinkUrl = uobDeeplinkUrl;
    }

    @SerializedName("uob_ezpay_deeplink_url")
    private String uobDeeplinkUrl;

    @SerializedName("pdf_url")
    private String pdfUrl;

    private String bank;
    private String eci;

    //for mandiri bill pay
    /**
     * bill number/code
     */
    @SerializedName("bill_key")
    private String paymentCode;

    /**
     * company or biller code.
     */
    @SerializedName("biller_code")
    private String companyCode;

    /**
     * payment code for Indomaret
     */
    @SerializedName("payment_code")
    private String paymentCodeResponse;

    @SerializedName("finish_redirect_url")
    private String finishRedirectUrl;

    @SerializedName("kioson_expire_time")
    private String kiosonExpireTime;

    @SerializedName("validation_messages")
    private ArrayList<String> validationMessages;

    @SerializedName("point_balance")
    private float pointBalance;
    @SerializedName("point_balance_amount")
    private String pointBalanceAmount;
    @SerializedName("point_redeem_amount")
    private float pointRedeemAmount;

    //for gopay
    @SerializedName("qr_code_url")
    private String qrCodeUrl;
    @SerializedName("deeplink_url")
    private String deeplinkUrl;
    //currently for shopeepay, could be used for other QRIS payment except gopay (gopay still using qr_code_url)
    @SerializedName("qris_url")
    private String qrisUrl;
    @SerializedName("qris_expiration_raw")
    private String qrisExpirationRaw;
    @SerializedName("qris_expiration")
    private String qrisExpiration;

    private String currency;

    public TransactionResponse() { }

    public TransactionResponse(
        String statusCode,
        String statusMessage,
        String transactionId,
        String orderId,
        String grossAmount,
        String paymentType,
        String transactionTime,
        String transactionStatus,
        String installmentTerm
    ) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.grossAmount = grossAmount;
        this.paymentType = paymentType;
        this.transactionTime = transactionTime;
        this.transactionStatus = transactionStatus;
        this.installmentTerm = installmentTerm;
    }

    /**
     * init transaction response by rba status
     *
     * @param statusMessage
     */
    public TransactionResponse(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusCode() {
        return TextUtils.isEmpty(statusCode) ? "" : statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return TextUtils.isEmpty(statusMessage) ? "" : statusMessage;
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

    public String getSavedTokenId() {
        return TextUtils.isEmpty(savedTokenId) ? "" : savedTokenId;
    }

    public void setSavedTokenId(String savedTokenId) {
        this.savedTokenId = savedTokenId;
    }

    public String getMaskedCard() {
        return maskedCard;
    }

    public void setMaskedCard(String maskedCard) {
        this.maskedCard = maskedCard;
    }

    public String getThreeDsVersion() {
        return threeDsVersion;
    }

    public void setThreeDsVersion(String threeDsVersion) {
        this.threeDsVersion = threeDsVersion;
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

    public String getPaymentType() {
        return TextUtils.isEmpty(paymentType) ? "" : paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransactionTime() {
        return TextUtils.isEmpty(transactionTime) ? "" : transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionStatus() {
        return TextUtils.isEmpty(transactionStatus) ? "" : transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getFraudStatus() {
        return TextUtils.isEmpty(fraudStatus) ? "" : fraudStatus;
    }

    public void setFraudStatus(String fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public String getSavedTokenIdExpiredAt() {
        return savedTokenIdExpiredAt;
    }

    public void setSavedTokenIdExpiredAt(String savedTokenIdExpiredAt) {
        this.savedTokenIdExpiredAt = savedTokenIdExpiredAt;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public boolean isSecureToken() {
        return secureToken;
    }

    public void setSecureToken(boolean secureToken) {
        this.secureToken = secureToken;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getEci() {
        return eci;
    }

    public void setEci(String eci) {
        this.eci = eci;
    }

    public String getPermataVANumber() {
        return permataVANumber;
    }

    public void setPermataVANumber(String permataVANumber) {
        this.permataVANumber = permataVANumber;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getString() {
        try {
            Gson gson = new Gson();
            return gson.toJson(this);
        } catch (NullPointerException e) {
            return "";
        }
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getPaymentCodeResponse() {
        return paymentCodeResponse;
    }

    public void setPaymentCodeResponse(String paymentCodeResponse) {
        this.paymentCodeResponse = paymentCodeResponse;
    }

    public List<VaNumber> getAccountNumbers() {
        return accountNumbers;
    }

    public void setAccountNumbers(List<VaNumber> accountNumbers) {
        this.accountNumbers = accountNumbers;
    }

    public String getFinishRedirectUrl() {
        return finishRedirectUrl;
    }

    public void setFinishRedirectUrl(String finishRedirectUrl) {
        this.finishRedirectUrl = finishRedirectUrl;
    }

    public String getXlTunaiOrderId() {
        return xlTunaiOrderId;
    }

    public void setXlTunaiOrderId(String xlTunaiOrderId) {
        this.xlTunaiOrderId = xlTunaiOrderId;
    }

    public String getXlTunaiMerchantId() {
        return xlTunaiMerchantId;
    }

    public void setXlTunaiMerchantId(String xlTunaiMerchantId) {
        this.xlTunaiMerchantId = xlTunaiMerchantId;
    }

    public String getXlTunaiExpiration() {
        return xlTunaiExpiration;
    }

    public void setXlTunaiExpiration(String xlTunaiExpiration) {
        this.xlTunaiExpiration = xlTunaiExpiration;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getKiosonExpireTime() {
        return kiosonExpireTime;
    }

    public void setKiosonExpireTime(String kiosonExpireTime) {
        this.kiosonExpireTime = kiosonExpireTime;
    }

    public ArrayList<String> getValidationMessages() {
        return validationMessages;
    }

    public void setValidationMessages(ArrayList<String> validationMessages) {
        this.validationMessages = validationMessages;
    }

    public String getInstallmentTerm() {
        return installmentTerm;
    }

    public String getPermataExpiration() {
        return permataExpiration;
    }

    public void setPermataExpiration(String permataExpiration) {
        this.permataExpiration = permataExpiration;
    }

    public String getBcaExpiration() {
        return bcaExpiration;
    }

    public void setBcaExpiration(String bcaExpiration) {
        this.bcaExpiration = bcaExpiration;
    }

    public String getMandiriBillExpiration() {
        return mandiriBillExpiration;
    }

    public void setMandiriBillExpiration(String mandiriBillExpiration) {
        this.mandiriBillExpiration = mandiriBillExpiration;
    }

    public String getIndomaretExpireTime() {
        return indomaretExpireTime;
    }

    public void setIndomaretExpireTime(String indomaretExpireTime) {
        this.indomaretExpireTime = indomaretExpireTime;
    }

    public String getBcaKlikBcaExpiration() {
        return bcaKlikBcaExpiration;
    }

    public void setBcaKlikBcaExpiration(String bcaKlikBcaExpiration) {
        this.bcaKlikBcaExpiration = bcaKlikBcaExpiration;
    }

    public float getPointBalance() {
        return pointBalance;
    }

    public void setPointBalance(float pointBalance) {
        this.pointBalance = pointBalance;
    }

    public String getPointBalanceAmount() {
        return pointBalanceAmount;
    }

    public void setPointBalanceAmount(String pointBalanceAmount) {
        this.pointBalanceAmount = pointBalanceAmount;
    }

    public float getPointRedeemAmount() {
        return pointRedeemAmount;
    }

    public void setPointRedeemAmount(float pointRedeemAmount) {
        this.pointRedeemAmount = pointRedeemAmount;
    }

    public String getBniVaNumber() {
        return bniVaNumber;
    }

    public void setBniVaNumber(String bniVaNumber) {
        this.bniVaNumber = bniVaNumber;
    }

    public String getBniExpiration() {
        return bniExpiration;
    }

    public void setBniExpiration(String bniExpiration) {
        this.bniExpiration = bniExpiration;
    }

    public String getBriVaNumber() {
        return briVaNumber;
    }

    public void setBriVaNumber(String briVaNumber) {
        this.briVaNumber = briVaNumber;
    }

    public String getBriExpiration() {
        return briExpiration;
    }

    public void setBriExpiration(String briExpiration) {
        this.briExpiration = briExpiration;
    }

    public String getBcaVaNumber() {
        return bcaVaNumber;
    }

    public void setBcaVaNumber(String bcaVaNumber) {
        this.bcaVaNumber = bcaVaNumber;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getDeeplinkUrl() {
        return deeplinkUrl;
    }

    public void setDeeplinkUrl(String deeplinkUrl) {
        this.deeplinkUrl = deeplinkUrl;
    }

    public void setQrisUrl(String qrisUrl) { this.qrisUrl = qrisUrl; }

    public String getQrisUrl() { return qrisUrl; }

    public String getQrisExpiration() {
        return qrisExpiration;
    }

    public void setQrisExpiration(String qrisExpiration) {
        this.qrisExpiration = qrisExpiration;
    }

    public String getQrisExpirationRaw() {
        return qrisExpirationRaw;
    }

    public void setQrisExpirationRaw(String qrisExpirationRaw) {
        this.qrisExpirationRaw = qrisExpirationRaw;
    }

    public String getGopayExpiration() {
        return gopayExpiration;
    }

    public void setGopayExpiration(String gopayExpiration) {
        this.gopayExpiration = gopayExpiration;
    }

    public String getGopayExpirationRaw() {
        return gopayExpirationRaw;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setInstallmentTerm(String installmentTerm) {
        this.installmentTerm = installmentTerm;
    }

    public String getAlfamartExpireTime() {
        return alfamartExpireTime;
    }

    public void setAlfamartExpireTime(String alfamartExpireTime) {
        this.alfamartExpireTime = alfamartExpireTime;
    }

    public void setGopayExpirationRaw(String gopayExpirationRaw) {
        this.gopayExpirationRaw = gopayExpirationRaw;
    }
}