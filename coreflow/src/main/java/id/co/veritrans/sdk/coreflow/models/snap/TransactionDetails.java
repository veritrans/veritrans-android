package id.co.veritrans.sdk.coreflow.models.snap;

/**
 * @author rakawm
 */
public class TransactionDetails {
    private String orderId;
    private String amount;

    public TransactionDetails() {
    }

    public TransactionDetails(String orderId, String amount) {
        setOrderId(orderId);
        setAmount(amount);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
