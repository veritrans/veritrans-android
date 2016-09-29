package com.midtrans.sdk.corekit.models.snap;

/**
 * @author rakawm
 */
public class PaymentOptions {
    private boolean creditCard3dSecure;
    private boolean saveCard;

    public PaymentOptions() {
    }

    public PaymentOptions(boolean creditCard3dSecure, boolean saveCard) {
        setCreditCard3dSecure(creditCard3dSecure);
        setSaveCard(saveCard);
    }

    public boolean isCreditCard3dSecure() {
        return creditCard3dSecure;
    }

    public void setCreditCard3dSecure(boolean creditCard3dSecure) {
        this.creditCard3dSecure = creditCard3dSecure;
    }

    public boolean isSaveCard() {
        return saveCard;
    }

    public void setSaveCard(boolean saveCard) {
        this.saveCard = saveCard;
    }
}
