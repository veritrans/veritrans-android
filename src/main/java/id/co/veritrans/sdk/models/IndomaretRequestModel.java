package id.co.veritrans.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shivam on 11/30/15.
 */
public class IndomaretRequestModel {


    /**
     * payment_type : cstore
     * transaction_details : {"order_id":"1388","gross_amount":100000}
     * cstore : {"store":"indomaret","message":"mangga"}
     */

    @SerializedName("payment_type")
    private String paymentType;
    /**
     * order_id : 1388
     * gross_amount : 100000
     */

    @SerializedName("transaction_details")
    private TransactionDetails transactionDetails;
    /**
     * store : indomaret
     * message : mangga
     */

    private CstoreEntity cstore;

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public void setCstore(CstoreEntity cstore) {
        this.cstore = cstore;
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

    public static class TransactionDetails {
        @SerializedName("order_id")
        private String orderId;
        @SerializedName("gross_amount")
        private int grossAmount;

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public void setGrossAmount(int grossAmount) {
            this.grossAmount = grossAmount;
        }

        public String getOrderId() {
            return orderId;
        }

        public int getGrossAmount() {
            return grossAmount;
        }
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
