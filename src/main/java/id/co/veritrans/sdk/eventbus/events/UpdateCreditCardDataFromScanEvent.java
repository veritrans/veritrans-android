package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.core.ExternalScanner;

/**
 * @author rakawm
 */
public class UpdateCreditCardDataFromScanEvent {
    private String cardNumber;
    private String cvv;
    private String expired;

    public UpdateCreditCardDataFromScanEvent(String cardNumber, String cvv, String expired) {
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
