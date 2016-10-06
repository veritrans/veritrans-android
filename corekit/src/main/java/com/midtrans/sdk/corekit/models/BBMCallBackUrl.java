package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 12/4/15.
 */
public class BBMCallBackUrl {
    @SerializedName("check_status")
    private String checkStatus;
    @SerializedName("before_payment_error")
    private String beforePaymentError;
    @SerializedName("user_cancel")
    private String userCancel;


    public BBMCallBackUrl(String checkStatus, String beforePaymentError, String userCancel) {
        this.checkStatus = checkStatus;
        this.beforePaymentError = beforePaymentError;
        this.userCancel = userCancel;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public String getBeforePaymentError() {
        return beforePaymentError;
    }


    public String getUserCancel() {
        return userCancel;
    }

}
