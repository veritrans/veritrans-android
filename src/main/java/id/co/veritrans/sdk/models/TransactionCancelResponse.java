package id.co.veritrans.sdk.models;

/**
 * Created by shivam on 10/26/15.
 */
public class TransactionCancelResponse {


    /**
     * status_message : Merchant cannot modify the status of the transaction
     * status_code : 412
     */
    private String status_message;
    private String status_code;

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    public String getStatus_code() {
        return status_code;
    }
}
