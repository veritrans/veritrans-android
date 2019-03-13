package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

public class CreditCardResponse extends BasePaymentResponse {

    public String getMaskedCard() {
        return maskedCard;
    }

    public void setMaskedCard(String maskedCard) {
        this.maskedCard = maskedCard;
    }

    public String getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(String fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Long getPointBalance() {
        return pointBalance;
    }

    public String getPointBalanceAmount() {
        return pointBalanceAmount;
    }

    public float getPointRedeemAmount() {
        return pointRedeemAmount;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getInstallmentTerm() {
        return installmentTerm;
    }

    public void setInstallmentTerm(String installmentTerm) {
        this.installmentTerm = installmentTerm;
    }

    public String getSavedTokenId() {
        return savedTokenId;
    }

    public void setSavedTokenId(String savedTokenId) {
        this.savedTokenId = savedTokenId;
    }
}