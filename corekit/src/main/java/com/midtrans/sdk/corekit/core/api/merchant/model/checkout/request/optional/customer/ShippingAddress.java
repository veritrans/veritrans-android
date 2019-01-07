package com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShippingAddress implements Serializable {

    private String address;
    private String city;
    private String phone;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("postal_code")
    private String postalCode;

    @SerializedName("country_code")
    private String countryCode;

    public ShippingAddress() {
    }

    public ShippingAddress(String address,
                           String city,
                           String phone,
                           String firstName,
                           String lastName,
                           String postalCode,
                           String countryCode) {
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}