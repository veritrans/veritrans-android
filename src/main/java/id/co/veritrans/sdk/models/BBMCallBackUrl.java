package id.co.veritrans.sdk.models;

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

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getBeforePaymentError() {
        return beforePaymentError;
    }

    public void setBeforePaymentError(String beforePaymentError) {
        this.beforePaymentError = beforePaymentError;
    }

    public String getUserCancel() {
        return userCancel;
    }

    public void setUserCancel(String userCancel) {
        this.userCancel = userCancel;
    }
}
