package id.co.veritrans.sdk.models;

/**
 * Created by shivam on 10/26/15.
 */
public class TransactionStatusResponse {


    /**
     * status_message : Success, transaction found
     * transaction_id : 39b690a3-d626-4577-a6ab-14e29a1c74ac
     * fraud_status : accept
     * approval_code : 1444915962534
     * transaction_status : cancel
     * status_code : 200
     * signature_key :
     * 2abb6348769ef9873b4a2a737a6822a13e9af66d24e1dac952a58e7c5a8dc7253a3b90e67db6726639d9cd60a7b2e6bcdbc7d09b31f31222183138cd95e94fcf
     * gross_amount : 100.00
     * payment_type : credit_card
     * bank : bni
     * masked_card : 481111-1114
     * transaction_time : 2015-10-15 20:32:34
     * order_id : 10938010
     */
    private String status_message;
    private String transaction_id;
    private String fraud_status;
    private String approval_code;
    private String transaction_status;
    private String status_code;
    private String signature_key;
    private String gross_amount;
    private String payment_type;
    private String bank;
    private String masked_card;
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

    public void setSignature_key(String signature_key) {
        this.signature_key = signature_key;
    }

    public void setGross_amount(String gross_amount) {
        this.gross_amount = gross_amount;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setMasked_card(String masked_card) {
        this.masked_card = masked_card;
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

    public String getSignature_key() {
        return signature_key;
    }

    public String getGross_amount() {
        return gross_amount;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public String getBank() {
        return bank;
    }

    public String getMasked_card() {
        return masked_card;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public String getOrder_id() {
        return order_id;
    }
}
