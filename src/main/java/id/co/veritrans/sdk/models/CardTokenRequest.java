package id.co.veritrans.sdk.models;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * model class hold information required to execute get token api call.
 *
 * Created by shivam on 10/29/15.
 */

public class CardTokenRequest implements Serializable {

    //card_number=4811111111111114
    // &card_cvv=123
    // &card_exp_month=12
    // &card_exp_year=2020
    // &client_key=VT-client-Lre_JFh5klhfGefF
    // &secure=true
    // &gross_amount=10000
    // &bank=bni
    // &two_click=false

    private String cardNumber = null;
    private int cardCVV = 0;
    private int cardExpiryMonth = 0;
    private int cardExpiryYear = 0;
    private String clientKey = null;
    private boolean secure = false;
    private boolean twoClick = false;
    private String bank = null;
    private double grossAmount = 0.0;
    private boolean isSaved = false;
    private String cardHolderName;
    private String cardType;
    private String savedTokenId;

    public CardTokenRequest(String cardNumber, int cardCVV,
                            int cardExpiryMonth, int cardExpiryYear, String clientKey) {

        this.cardNumber = cardNumber;
        this.cardCVV = cardCVV;
        this.cardExpiryMonth = cardExpiryMonth;
        this.cardExpiryYear = cardExpiryYear;
        this.clientKey = clientKey;
    }

    //card_cvv, token_id, two_click, bank, secure, gross_amount
   /* public CardTokenRequest(int cardCVV, String token, boolean isTwoClick, boolean isSecure,
   String bank, double grossAmount) {

        this.cardCVV = cardCVV;
        this.savedTokenId = token;
        this.twoClick = isTwoClick;
        this.secure = isSecure;
        this.bank = bank;
        this.grossAmount = grossAmount;
    }*/

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCardCVV() {
        return cardCVV;
    }

    public void setCardCVV(int cardCVV) {
        this.cardCVV = cardCVV;
    }

    public int getCardExpiryMonth() {
        return cardExpiryMonth;
    }

    public int getCardExpiryYear() {
        return cardExpiryYear;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    /**
     * if transaction is 3ds then it will return true.
     *
     * @return boolean value.
     */
    public boolean isSecure() {
        return secure;
    }

    /**
     * if 3ds transaction set it true.
     *
     * @param secure
     */
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isTwoClick() {
        return twoClick;
    }

    public void setTwoClick(boolean twoClick) {
        this.twoClick = twoClick;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(double grossAmount) {
        this.grossAmount = grossAmount;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getFormatedCardNumber() {
        //String startString = "";
        String endString = "";

        if (!TextUtils.isEmpty(cardNumber) && cardNumber.length() == 16) {
            //  startString = cardNumber.substring(0,3);
            endString = cardNumber.substring(12, 16);
        }
        return "XXXX-XXXX-XXXX-" + endString;
    }

    public String getFormatedExpiryDate() {
        if (getCardExpiryYear() > 0) {
            return "XX/" + getCardExpiryYear();

        }
        return "XX/XX";
    }

    public String getSavedTokenId() {
        return savedTokenId;
    }

    public void setSavedTokenId(String savedTokenId) {
        this.savedTokenId = savedTokenId;
    }

    public String getString() {
        try {
            Gson gson = new Gson();
            return gson.toJson(this);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "";
        }
    }
}