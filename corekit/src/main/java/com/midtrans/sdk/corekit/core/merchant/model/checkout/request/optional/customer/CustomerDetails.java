package com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.customer;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerDetails implements Serializable {

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;
    private String email;
    private String phone;

    @SerializedName("shipping_address")
    private ShippingAddress shippingAddress;
    @SerializedName("billing_address")
    private BillingAddress billingAddress;

    private CustomerDetails(String firstName,
                            String lastName,
                            String email,
                            String phone,
                            ShippingAddress shippingAddress,
                            BillingAddress billingAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
    }

    public static Builder builder(String firstName, String lastName, String email, String phone) {
        return new Builder(firstName, lastName, email, phone);
    }

    public static class Builder {
        private ShippingAddress shippingAddress;
        private BillingAddress billingAddress;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;

        public Builder(String firstName, String lastName, String email, String phone) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;
        }

        public Builder setShippingAddress(ShippingAddress shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public Builder setBillingAddress(BillingAddress billingAddress) {
            this.billingAddress = billingAddress;
            return this;
        }

        public CustomerDetails build() {
            return new CustomerDetails(firstName, lastName, email, phone, shippingAddress, billingAddress);
        }
    }

    public CustomerDetails(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }
}