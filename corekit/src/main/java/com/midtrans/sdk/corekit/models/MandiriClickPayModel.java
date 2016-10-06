package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * contains information related mandiri click pay.
 *
 * Created by shivam on 11/2/15.
 */
public class MandiriClickPayModel {
    /**
     * cardNumber : 4111111111111111 input3 : 11111 input2 : 100 input1 : 1111111111 token : 111111
     */


    @SerializedName("card_number")
    private String cardNumber;

    private String input3;
    private String input2;
    private String input1;
    private String token;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getInput3() {
        return input3;
    }

    public void setInput3(String input3) {
        this.input3 = input3;
    }

    public String getInput2() {
        return input2;
    }

    public void setInput2(String input2) {
        //required amount in integer format
        if (input2 != null) {
            this.input2 = "" + (int) Double.parseDouble(input2);
        } else {
            this.input2 = "";
        }
    }

    public String getInput1() {
        return input1;
    }

    public void setInput1(String input1) {
        this.input1 = input1;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}