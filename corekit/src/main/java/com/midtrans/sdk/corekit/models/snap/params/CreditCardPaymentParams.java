package com.midtrans.sdk.corekit.models.snap.params;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 9/30/16.
 */

public class CreditCardPaymentParams {

    @SerializedName("masked_card")
    private String maskedCard;
    @SerializedName("card_token")
    private String cardToken;
    private String bank;
    @SerializedName("installment")
    private String installmentTerm;
    @SerializedName("save_card")
    private boolean saveCard;
    @SerializedName("point")
    private float pointRedeemed;
    private transient boolean isFromBankPoint;

    public CreditCardPaymentParams(String cardToken, Boolean saveCard, String maskedCardNumber) {
        this.cardToken = cardToken;
        this.saveCard = saveCard;
        this.maskedCard = maskedCardNumber;
    }

    public CreditCardPaymentParams(String cardToken, Boolean saveCard, String maskedCardNumber, String installmentTerm) {
        this.cardToken = cardToken;
        this.saveCard = saveCard;
        this.maskedCard = maskedCardNumber;
        this.installmentTerm = installmentTerm;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public boolean isSaveCard() {
        return saveCard;
    }

    public void setSaveCard(boolean saveCard) {
        this.saveCard = saveCard;
    }

    public String getMaskedCard() {
        return maskedCard;
    }

    public void setMaskedCard(String maskedCard) {
        this.maskedCard = maskedCard;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getInstallmentTerm() {
        return installmentTerm;
    }

    public void setInstallmentTerm(String installmentTerm) {
        this.installmentTerm = installmentTerm;
    }

    public float getPointRedeemed() {
        return pointRedeemed;
    }

    public void setPointRedeemed(float pointRedeemed) {
        this.pointRedeemed = pointRedeemed;
    }

    public boolean isFromBankPoint() {
        return isFromBankPoint;
    }

    public void setFromBankPoint(boolean fromBankPoint) {
        isFromBankPoint = fromBankPoint;
    }
}
