package id.co.veritrans.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shivam on 11/2/15.
 */
public class MandiriClickPayModel {
    /**
     * cardNumber : 4111111111111111
     * input3 : 11111
     * input2 : 100
     * input1 : 1111111111
     * token : 111111
     */


    @SerializedName("card_number")
    private String cardNumber;
    private String input3;
    private String input2;
    private String input1;
    private String token;

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setInput3(String input3) {
        this.input3 = input3;
    }

    public void setInput2(String input2) {
        this.input2 = input2;
    }

    public void setInput1(String input1) {
        this.input1 = input1;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getInput3() {
        return input3;
    }

    public String getInput2() {
        return input2;
    }

    public String getInput1() {
        return input1;
    }

    public String getToken() {
        return token;
    }
}