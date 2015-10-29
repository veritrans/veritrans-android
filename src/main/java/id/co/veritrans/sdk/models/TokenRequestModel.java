package id.co.veritrans.sdk.models;

/**
 * Created by shivam on 10/29/15.
 */

public class TokenRequestModel {


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


    public TokenRequestModel(String  cardNumber, int cardCVV,
                             int cardExpiryMonth, int cardExpiryYear, String clientKey){

        this.cardNumber = cardNumber;
        this.cardCVV = cardCVV;
        this.cardExpiryMonth = cardExpiryMonth;
        this.cardExpiryYear = cardExpiryYear;
        this.clientKey = clientKey;
    }

    public String getCardNumber() {
        return cardNumber;
    }


    public int getCardCVV() {
        return cardCVV;
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


    /**
     * if transaction is 3ds then it will return true.
     * @return boolean value.
     */
    public boolean isSecure() {
        return secure;
    }

    /**
     * if 3ds transaction set it true.
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
}