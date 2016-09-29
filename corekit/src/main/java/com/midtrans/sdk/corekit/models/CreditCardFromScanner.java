package com.midtrans.sdk.corekit.models;

/**
 * Created by ziahaqi on 9/2/16.
 */
public class CreditCardFromScanner {
    private String cardNumber;
    private String cvv;
    private String expired;

    public CreditCardFromScanner(String cardNumber, String cvv, String expired) {
        setCardNumber(cardNumber);
        setCvv(cvv);
        setExpired(expired);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }
}
