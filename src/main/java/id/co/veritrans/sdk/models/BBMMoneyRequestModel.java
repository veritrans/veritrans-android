package id.co.veritrans.sdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shivam on 11/30/15.
 */
public class BBMMoneyRequestModel {


    @SerializedName("payment_type")
    private String paymentType;

    @SerializedName("transaction_details")
    private TransactionDetails transactionDetails;

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }
}
