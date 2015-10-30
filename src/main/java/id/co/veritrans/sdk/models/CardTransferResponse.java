package id.co.veritrans.sdk.models;

/**
 * Created by shivam on 10/30/15.
 */
public class CardTransferResponse {

    /**
     * status_message : Success, Credit Card transaction is successful
     * transaction_id : 1944a3b4-8534-42d1-b251-1dfcbdc9af0c
     * fraud_status : accept
     * approval_code : 1446184074456
     * transaction_status : capture
     * status_code : 200
     * saved_token_id_expired_at : 2025-10-30 12:47:55
     * gross_amount : 10000.00
     * payment_type : credit_card
     * secure_token : false
     * bank : bni
     * masked_card : 481111-1114
     * saved_token_id : 4811114c66e481-829a-4072-83dd-5f10fa7a0a15
     * transaction_time : 2015-10-30 12:47:51
     * order_id : 10938039
     */
    private String status_message;
    private String transaction_id;
    private String fraud_status;
    private String approval_code;
    private String transaction_status;
    private String status_code;
    private String saved_token_id_expired_at;
    private String gross_amount;
    private String payment_type;
    private boolean secure_token;
    private String bank;
    private String masked_card;
    private String saved_token_id;
    private String transaction_time;
    private String order_id;

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public void setFraud_status(String fraud_status) {
        this.fraud_status = fraud_status;
    }

    public void setApproval_code(String approval_code) {
        this.approval_code = approval_code;
    }

    public void setTransaction_status(String transaction_status) {
        this.transaction_status = transaction_status;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public void setSaved_token_id_expired_at(String saved_token_id_expired_at) {
        this.saved_token_id_expired_at = saved_token_id_expired_at;
    }

    public void setGross_amount(String gross_amount) {
        this.gross_amount = gross_amount;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public void setSecure_token(boolean secure_token) {
        this.secure_token = secure_token;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setMasked_card(String masked_card) {
        this.masked_card = masked_card;
    }

    public void setSaved_token_id(String saved_token_id) {
        this.saved_token_id = saved_token_id;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
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

    public String getFraud_status() {
        return fraud_status;
    }

    public String getApproval_code() {
        return approval_code;
    }

    public String getTransaction_status() {
        return transaction_status;
    }

    public String getStatus_code() {
        return status_code;
    }

    public String getSaved_token_id_expired_at() {
        return saved_token_id_expired_at;
    }

    public String getGross_amount() {
        return gross_amount;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public boolean isSecure_token() {
        return secure_token;
    }

    public String getBank() {
        return bank;
    }

    public String getMasked_card() {
        return masked_card;
    }

    public String getSaved_token_id() {
        return saved_token_id;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public String getOrder_id() {
        return order_id;
    }
}