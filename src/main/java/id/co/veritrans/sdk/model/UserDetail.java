package id.co.veritrans.sdk.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by chetan on 19/10/15.
 */
public class UserDetail implements Serializable {
    private String userFullName;
    private String email;
    private String phoneNumber;

    public String getUserFullName() {
        return TextUtils.isEmpty(userFullName) ? "" : userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getEmail() {
        return TextUtils.isEmpty(email) ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
