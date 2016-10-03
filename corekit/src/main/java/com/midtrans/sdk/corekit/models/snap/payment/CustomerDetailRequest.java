package com.midtrans.sdk.corekit.models.snap.payment;

import com.google.gson.annotations.SerializedName;

/**
 * @author rakawm
 */
public class CustomerDetailRequest {
    @SerializedName("full_name")
    private String fullName;
    private String email;
    private String phone;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
