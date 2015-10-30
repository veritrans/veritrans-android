package id.co.veritrans.sdk.models;

/**
 * Created by shivam on 10/29/15.
 */
public class TransactionDetails {


    /**
     * gross_amount : 100
     * order_id : 10938011
     */
    public TransactionDetails() {

    }
    public TransactionDetails(String gross_amount, String order_id) {
        this.gross_amount = gross_amount;
        this.order_id = order_id;
    }

    private String gross_amount;
    private String order_id;

    public void setGross_amount(String gross_amount) {
        this.gross_amount = gross_amount;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getGross_amount() {
        return gross_amount;
    }

    public String getOrder_id() {
        return order_id;
    }
}
