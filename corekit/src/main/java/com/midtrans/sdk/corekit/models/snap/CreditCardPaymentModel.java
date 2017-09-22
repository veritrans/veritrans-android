package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 10/17/16.
 *
 * model for creditcard transaction
 */

public class CreditCardPaymentModel {

    private String cardToken;
    private boolean savecard;
    private String maskedCardNumber;
    private String installment;
    private String bank;
    private transient boolean isFromBankPoint;
    @SerializedName("point")
    private float pointRedeemed;

    /**
     * init credit card model for normal and twoclick payment
     *
     * @param cardToken credit card token form PAPI
     * @param savecard save card for next transaction
     */
    public CreditCardPaymentModel(String cardToken, boolean savecard) {
        this.cardToken = cardToken;
        this.savecard = savecard;
    }

    /**
     * init credit card model for oneclick payment
     *
     * @param maskedCardNumber masked credit card number
     */
    public CreditCardPaymentModel(String maskedCardNumber) {
        this.maskedCardNumber = maskedCardNumber;
    }

    public String getCardToken() {
        return cardToken;
    }

    public boolean isSavecard() {
        return savecard;
    }

    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }

    public float getPointRedeemed() {
        return pointRedeemed;
    }

    public void setPointRedeemed(float pointRedeemed) {
        this.pointRedeemed = pointRedeemed;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public boolean isFromBankPoint() {
        return isFromBankPoint;
    }

    public void setFromBankPoint(boolean fromBankPoint) {
        isFromBankPoint = fromBankPoint;
    }
}
