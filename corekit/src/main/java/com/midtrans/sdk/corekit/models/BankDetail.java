package com.midtrans.sdk.corekit.models;

/**
 * Created by chetan on 10/11/15.
 */

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Id,Bin,Issuing bank,Country,Bin type,Bin class,Card association,Created at,Updated at,Bank code
 */
public class BankDetail implements Serializable {

    /**
     * Issuing_bank : BANK ANZ INDONESIA Country : Indonesia Bin_type : Bin_class : Card_association
     * : VISA Created_at : 2014-02-06 16:06:56 +0700 Updated_at : 2015-07-02 13:21:38 +0700
     * Bank_code :
     */
    private String Id;
    private String Bin;
    private String Issuing_bank;
    private String Country;
    private String Bin_type;
    private String Bin_class;
    private String Card_association;
    private String Created_at;
    private String Updated_at;
    private String Bank_code;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getBin() {
        return TextUtils.isEmpty(Bin) ? "" : Bin;
    }

    public void setBin(String bin) {
        Bin = bin;
    }

    public String getIssuing_bank() {
        return TextUtils.isEmpty(Issuing_bank) ? "" : Issuing_bank;
    }

    public void setIssuing_bank(String Issuing_bank) {
        this.Issuing_bank = Issuing_bank;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getBin_type() {
        return Bin_type;
    }

    public void setBin_type(String Bin_type) {
        this.Bin_type = Bin_type;
    }

    public String getBin_class() {
        return Bin_class;
    }

    public void setBin_class(String Bin_class) {
        this.Bin_class = Bin_class;
    }

    public String getCard_association() {
        return TextUtils.isEmpty(Card_association) ? "" : Card_association;
    }

    public void setCard_association(String Card_association) {
        this.Card_association = Card_association;
    }

    public String getCreated_at() {
        return Created_at;
    }

    public void setCreated_at(String Created_at) {
        this.Created_at = Created_at;
    }

    public String getUpdated_at() {
        return Updated_at;
    }

    public void setUpdated_at(String Updated_at) {
        this.Updated_at = Updated_at;
    }

    public String getBank_code() {
        return Bank_code;
    }

    public void setBank_code(String Bank_code) {
        this.Bank_code = Bank_code;
    }
}
