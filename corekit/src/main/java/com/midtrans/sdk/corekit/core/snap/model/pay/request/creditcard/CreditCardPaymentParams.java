package com.midtrans.sdk.corekit.core.snap.model.pay.request.creditcard;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.base.model.BankType;

public class CreditCardPaymentParams {
    @SerializedName("masked_card")
    private String maskedCard;
    @SerializedName("card_token")
    private String cardToken;
    private String bank = BankType.BNI;
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
        return this.cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public boolean isSaveCard() {
        return this.saveCard;
    }

    public void setSaveCard(boolean saveCard) {
        this.saveCard = saveCard;
    }

    public String getMaskedCard() {
        return this.maskedCard;
    }

    public void setMaskedCard(String maskedCard) {
        this.maskedCard = maskedCard;
    }

    public String getBank() {
        return this.bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getInstallmentTerm() {
        return this.installmentTerm;
    }

    public void setInstallmentTerm(String installmentTerm) {
        this.installmentTerm = installmentTerm;
    }

    public float getPointRedeemed() {
        return this.pointRedeemed;
    }

    public void setPointRedeemed(float pointRedeemed) {
        this.pointRedeemed = pointRedeemed;
    }

    public boolean isFromBankPoint() {
        return this.isFromBankPoint;
    }

    public void setFromBankPoint(boolean fromBankPoint) {
        this.isFromBankPoint = fromBankPoint;
    }
}
