package id.co.veritrans.sdk.models;

/**
 * Created by shivam on 10/26/15.
 */
public class PermataBankTransferResponse {


    /**
     * status_message : Success, PERMATA VA transaction is successful
     * transaction_id : f7772483-413b-4ad1-bda2-e7137ae65700
     * payment_type : bank_transfer
     * transaction_status : pending
     * status_code : 201
     * permata_va_number : 8778006020203963
     * transaction_time : 2015-10-26 16:43:43
     * gross_amount : 100.00
     * order_id : 10938011
     */
    private String status_message;
    private String transaction_id;
    private String payment_type;
    private String transaction_status;
    private String status_code;
    private String permata_va_number;
    private String transaction_time;
    private String gross_amount;
    private String order_id;

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public void setTransaction_status(String transaction_status) {
        this.transaction_status = transaction_status;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public void setPermata_va_number(String permata_va_number) {
        this.permata_va_number = permata_va_number;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }

    public void setGross_amount(String gross_amount) {
        this.gross_amount = gross_amount;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus_message() {
        return status_message;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public String getTransaction_status() {
        return transaction_status;
    }

    public String getStatus_code() {
        return status_code;
    }

    public String getPermata_va_number() {
        return permata_va_number;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public String getGross_amount() {
        return gross_amount;
    }

    public String getOrder_id() {
        return order_id;
    }
}
