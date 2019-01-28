package com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address implements Serializable {

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

    public static Builder builder() {
        return new Builder();
    }

    private Address(String firstName,
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public static class Builder {
        private String firstName;
        private String lastName;
        private String address;
        private String city;
        private String postalCode;
        private String phone;
        private String countryCode;

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setCountryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public Address build() {
            return new Address(
                    firstName,
                    lastName,
                    address,
                    city,
                    postalCode,
                    phone,
                    countryCode
            );
        }
    }
}