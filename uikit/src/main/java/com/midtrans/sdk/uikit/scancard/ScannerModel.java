package com.midtrans.sdk.uikit.scancard;

import java.io.Serializable;

/**
 * @author rakawm
 */
public class ScannerModel implements Serializable {
    private String cardNumber;
    private String cvv;
    private int expiredMonth;
    private int expiredYear;

    public ScannerModel(String cardNumber, String cvv, int expiredMonth, int expiredYear) {
        setCardNumber(cardNumber);
        setCvv(cvv);
        setExpiredMonth(expiredMonth);
        setExpiredYear(expiredYear);
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

    public int getExpiredMonth() {
        return expiredMonth;
    }

    public void setExpiredMonth(int expiredMonth) {
        this.expiredMonth = expiredMonth;
    }

    public int getExpiredYear() {
        return expiredYear;
    }

    public void setExpiredYear(int expiredYear) {
        this.expiredYear = expiredYear;
    }
}
