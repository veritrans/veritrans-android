package com.midtrans.sdk.uikit.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ziahaqi on 7/29/16.
 */
public class CountryCodeModel implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("alpha-3")
    private String countryCodeAlpha;
    @SerializedName("country-code")
    private String countryCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCodeAlpha() {
        return countryCodeAlpha;
    }

    public void setCountryCodeAlpha(String countryCodeAlpha) {
        this.countryCodeAlpha = countryCodeAlpha;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
