package id.co.veritrans.sdk.models;

import android.text.TextUtils;

/**
 * Created by chetan on 20/11/15.
 */
public class TransactionUpdateMerchantResponse {
    private String message;
    private String error;

    public String getMessage() {
        return !TextUtils.isEmpty(message) ? message : "";
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return !TextUtils.isEmpty(error) ? error : "";
    }

    public void setError(String error) {
        this.error = error;
    }
}
