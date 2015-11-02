package id.co.veritrans.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shivam on 10/26/15.
 */
public class TransactionCancelResponse {


    /**
     * statusMessage : Merchant cannot modify the status of the transaction
     * status_code : 412
     */
    @SerializedName("status_message")
    private String statusMessage;

    @SerializedName("status_code")
    private String statusCode;

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setStatus_code(String status_code) {
        this.statusCode = status_code;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
