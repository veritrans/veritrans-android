package com.midtrans.sdk.core.models.snap.card;

/**
 * Created by rakawm on 10/19/16.
 */

public class CreditCardPaymentParams {
    private String cardToken;
    private String installment;
    private boolean saveCard;
    private String maskedCard;
    private float point;


    /**
     * Create basic payment params.
     *
     * @param cardToken card token.
     * @return credit card payment params.
     */
    public static CreditCardPaymentParams newBasicPaymentParams(String cardToken) {
        CreditCardPaymentParams creditCardPaymentParams = new CreditCardPaymentParams();
        creditCardPaymentParams.setCardToken(cardToken);
        return creditCardPaymentParams;
    }

    /**
     * Create installment payment params.
     *
     * @param cardToken       card token.
     * @param installmentTerm installment term. Format: {{BANK_NAME}}_{{TERM}}
     * @return credit card payment params.
     */
    public static CreditCardPaymentParams newInstallmentPaymentParams(String cardToken, String installmentTerm) {
        CreditCardPaymentParams creditCardPaymentParams = new CreditCardPaymentParams();
        creditCardPaymentParams.setCardToken(cardToken);
        creditCardPaymentParams.setInstallment(installmentTerm);
        return creditCardPaymentParams;
    }

    /**
     * Create credit card payment param
     *
     * @param maskedCardNumber masked card number.
     *
     * @return credit card payment params.
     */
    public static CreditCardPaymentParams newOneClickPaymentParams(String maskedCardNumber) {
        CreditCardPaymentParams creditCardPaymentParams = new CreditCardPaymentParams();
        creditCardPaymentParams.setMaskedCard(maskedCardNumber);
        return creditCardPaymentParams;
    }

    /**
     * Create credit card payment param.
     *
     * @param cardToken card token.
     * @param point     redeemed point.
     * @return credit card payment param.
     */
    public static CreditCardPaymentParams newBankPointPaymentParams(String cardToken, float point) {
        CreditCardPaymentParams creditCardPaymentParams = new CreditCardPaymentParams();
        creditCardPaymentParams.setCardToken(cardToken);
        creditCardPaymentParams.setPoint(point);
        return creditCardPaymentParams;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }

    public boolean isSaveCard() {
        return saveCard;
    }

    public void setSaveCard(boolean saveCard) {
        this.saveCard = saveCard;
    }

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    public String getMaskedCard() {
        return maskedCard;
    }

    public void setMaskedCard(String maskedCard) {
        this.maskedCard = maskedCard;
    }
}
