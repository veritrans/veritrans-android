package id.co.veritrans.sdk.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by chetan on 19/10/15.
 */
public class UserAddress implements Serializable {
    private String address;
    private String city;
    private String zipcode;
    private String country;
    private boolean isBookingAddress;
    private boolean isShippingAddress;

    public String getAddress() {
        return TextUtils.isEmpty(address) ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return TextUtils.isEmpty(city) ? "" : city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return TextUtils.isEmpty(zipcode) ? "" : zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return TextUtils.isEmpty(country) ? "" : country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isBookingAddress() {
        return isBookingAddress;
    }

    public void setIsBookingAddress(boolean isBookingAddress) {
        this.isBookingAddress = isBookingAddress;
    }

    public boolean isShippingAddress() {
        return isShippingAddress;
    }

    public void setIsShippingAddress(boolean isShippingAddress) {
        this.isShippingAddress = isShippingAddress;
    }
}
