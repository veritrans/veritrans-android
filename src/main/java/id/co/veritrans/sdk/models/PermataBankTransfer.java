package id.co.veritrans.sdk.models;

/**
 * Created by shivam on 10/26/15.
 */
public class PermataBankTransfer {


    /**
     * payment_type : bank_transfer
     * bank_transfer : {"bank":"permata"}
     * transaction_details : {"gross_amount":"100","order_id":"10938011"}
     */
    private String payment_type;
    private Bank_transferEntity bank_transfer;
    private Transaction_detailsEntity transaction_details;

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public void setBank_transfer(Bank_transferEntity bank_transfer) {
        this.bank_transfer = bank_transfer;
    }

    public void setTransaction_details(Transaction_detailsEntity transaction_details) {
        this.transaction_details = transaction_details;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public Bank_transferEntity getBank_transfer() {
        return bank_transfer;
    }

    public Transaction_detailsEntity getTransaction_details() {
        return transaction_details;
    }

    public class Bank_transferEntity {
        /**
         * bank : permata
         */
        private String bank;

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getBank() {
            return bank;
        }
    }

    public class Transaction_detailsEntity {
        /**
         * gross_amount : 100
         * order_id : 10938011
         */
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
}
