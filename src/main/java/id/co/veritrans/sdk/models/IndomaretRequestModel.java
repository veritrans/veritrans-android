package id.co.veritrans.sdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shivam on 11/30/15.
 */
public class IndomaretRequestModel {


    /**
     * email :
     * first_name : shs
     * last_name :
     * phone : 8576945461
     */

    @SerializedName("customer_details")
    private CustomerDetails customerDetails;
    /**
     * customer_details : {"email":"","first_name":"shs","last_name":" ","phone":"8576945461"}
     * item_details : [{"id":"1","name":"pen","price":1000,"quantity":1}]
     * payment_type : cstore
     * transaction_details : {"gross_amount":"1000.0","order_id":"980c31f50f7"}
     * cstore : {"store":"indomaret","message":"mangga"}
     */

    @SerializedName("payment_type")
    private String paymentType;
    /**
     * gross_amount : 1000.0
     * order_id : 980c31f50f7
     */

    @SerializedName("transaction_details")
    private TransactionDetails transactionDetails;
    /**
     * store : indomaret
     * message : mangga
     */

    private CstoreEntity cstore;
    /**
     * id : 1
     * name : pen
     * price : 1000.0
     * quantity : 1.0
     */

    private List<ItemDetails> item_details;

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public void setCstore(CstoreEntity cstore) {
        this.cstore = cstore;
    }

    public void setItem_details(List<ItemDetails> item_details) {
        this.item_details = item_details;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public CstoreEntity getCstore() {
        return cstore;
    }

    public List<ItemDetails> getItem_details() {
        return item_details;
    }



    public static class CstoreEntity {
        private String store;
        private String message;

        public void setStore(String store) {
            this.store = store;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStore() {
            return store;
        }

        public String getMessage() {
            return message;
        }
    }


}
