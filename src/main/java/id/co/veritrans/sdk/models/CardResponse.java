package id.co.veritrans.sdk.models;

import java.util.ArrayList;

/**
 * Created by chetan on 04/12/15.
 */
public class CardResponse {
    private String error;
    private String message;
    private ArrayList<CardTokenRequest> cards;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<CardTokenRequest> getCreditCards() {
        return cards;
    }

    public void setCreditCards(ArrayList<CardTokenRequest> creditCards) {
        this.cards = creditCards;
    }
}
