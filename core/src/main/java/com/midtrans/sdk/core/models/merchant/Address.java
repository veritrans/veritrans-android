package com.midtrans.sdk.core.models.merchant;

/**
 * Created by rakawm on 10/19/16.
 */

public class Address {
    public final String firstName;
    public final String lastName;
    public final String address;
    public final String city;
    public final String postalCode;
    public final String phone;
    public final String countryCode;

    public Address(String firstName,
                   String lastName,
                   String address,
                   String city,
                   String postalCode,
                   String phone,
                   String countryCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
        this.countryCode = countryCode;
    }
}
