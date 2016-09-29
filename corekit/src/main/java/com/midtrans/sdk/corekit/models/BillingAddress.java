package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * It holds an information about billing address of user.
 *
 * Created by shivam on 10/29/15.
 */
public class BillingAddress {

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private String address;
    private String city;

    @SerializedName("postal_code")
    private String postalCode;
    private String phone;

    @SerializedName("country_code")
    private String countryCode;


    public BillingAddress() {

    }

    public BillingAddress(String firstName, String lastName, String address, String city, String
            postalCode, String phone, String countryCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
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