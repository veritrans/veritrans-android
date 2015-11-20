package id.co.veritrans.sdk.models;

/**
 * Created by chetan on 20/11/15.
 */
public class TransactionMerchant {
    private String orderId;
    private String transactionId;  //id received after successful transaction,
    private String grossAmount ;// amount of money paid,
    private String paymentType ;// like (credit_card,bank_transfer etc),
    private String userEmail; //user email to relate with user,
    private String transactionTime; // time when transaction occurred.
    private String transactionStatus;

    public TransactionMerchant(String orderId, String transactionId, String grossAmount,
                               String paymentType, String userEmail, String transactionTime,String transactionStatus) {
        this.orderId = orderId;
        this.transactionId = transactionId;
        this.grossAmount = grossAmount;
        this.paymentType = paymentType;
        this.userEmail = userEmail;
        this.transactionTime = transactionTime;
        this.transactionStatus = transactionStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
