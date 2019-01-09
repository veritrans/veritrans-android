package com.midtrans.sdk.corekit.core.api.snap.model.pay.request;

import com.google.gson.annotations.SerializedName;

import android.text.TextUtils;

import java.io.Serializable;

public class CustomerDetailPayRequest implements Serializable {
    @SerializedName("full_name")
    private String fullName;
    private String email;
    private String phone;

    public CustomerDetailPayRequest() {
    }

    public CustomerDetailPayRequest(String fullName,
                                    String email,
                                    String phone) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = TextUtils.isEmpty(fullName) ? null : fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = TextUtils.isEmpty(email) ? null : email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = TextUtils.isEmpty(phone) ? null : phone;
    }
}