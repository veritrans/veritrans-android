package com.midtrans.sdk.corekit.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by chetan on 19/10/15.
 */
public class UserDetail implements Serializable {
    private String userFullName;
    private String email;
    private String phoneNumber;
    private ArrayList<UserAddress> userAddresses;
    private String merchantToken;//stored after merchant register api call
    private String userId;

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<UserAddress> getUserAddresses() {
        return userAddresses;
    }

    public void setUserAddresses(ArrayList<UserAddress> userAddresses) {
        this.userAddresses = userAddresses;
    }

    public String getMerchantToken() {
        return merchantToken;
    }

    public void setMerchantToken(String merchantToken) {
        this.merchantToken = merchantToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
