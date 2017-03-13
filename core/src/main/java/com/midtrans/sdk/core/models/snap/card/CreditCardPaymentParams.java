package com.midtrans.sdk.core.models.snap.card;

/**
 * Created by rakawm on 10/19/16.
 */

public class CreditCardPaymentParams {
    private String cardToken;
    private String installment;
    private boolean saveCard;
    private String maskedCard;


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
     * @return credit card payment params.
     */
    public static CreditCardPaymentParams newOneClickPaymentParams(String maskedCardNumber) {
        CreditCardPaymentParams creditCardPaymentParams = new CreditCardPaymentParams();
        creditCardPaymentParams.setMaskedCard(maskedCardNumber);
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

    public void setMaskedCard(String maskedCard) {
        this.maskedCard = maskedCard;
    }
}
