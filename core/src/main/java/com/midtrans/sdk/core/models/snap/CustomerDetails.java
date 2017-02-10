package com.midtrans.sdk.core.models.snap;

/**
 * Created by rakawm on 10/19/16.
 */

public class CustomerDetails {
    public final String firstName;
    public final String lastName;
    public final String email;
    public final String phone;
    public final Address shippingAddress;
    public final Address billingAddress;

    public CustomerDetails(String firstName, String lastName, String email, String phone, Address shippingAddress, Address billingAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
    }
}
