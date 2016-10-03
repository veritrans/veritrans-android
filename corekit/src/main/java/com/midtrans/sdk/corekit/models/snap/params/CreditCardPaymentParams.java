package com.midtrans.sdk.corekit.models.snap.params;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 9/30/16.
 */

public class CreditCardPaymentParams {
    @SerializedName("card_token")
    private String cardToken;

    private String bank;
    @SerializedName("installment_term")
    private String  installmentTerm;
    @SerializedName("save_card")
    private boolean saveCard;

    public CreditCardPaymentParams(String cardToken, Boolean saveCard) {
        this.cardToken = cardToken;
        this.saveCard = saveCard;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setInstallmentTerm(String installmentTerm) {
        this.installmentTerm = installmentTerm;
    }

    public void setSaveCard(boolean saveCard) {
        this.saveCard = saveCard;
    }

    public String getCardToken() {
        return cardToken;
    }

    public boolean isSaveCard() {
        return saveCard;
    }
}
