package com.midtrans.sdk.core.models.snap;

/**
 * Created by rakawm on 10/19/16.
 */

public class SnapCustomerDetails {
    public final String fullName;
    public final String email;
    public final String phone;

    public SnapCustomerDetails(String fullName, String email, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }
}
