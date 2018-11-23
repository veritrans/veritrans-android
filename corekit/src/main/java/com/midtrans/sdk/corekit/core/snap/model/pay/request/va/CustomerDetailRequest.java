package com.midtrans.sdk.corekit.core.snap.model.pay.request.va;

import com.google.gson.annotations.SerializedName;

import android.text.TextUtils;

public class CustomerDetailRequest {
    @SerializedName("full_name")
    private String fullName;
    private String email;
    private String phone;

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
