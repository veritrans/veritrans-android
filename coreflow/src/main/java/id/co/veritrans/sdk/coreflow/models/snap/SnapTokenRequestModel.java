package id.co.veritrans.sdk.coreflow.models.snap;

import com.google.gson.annotations.SerializedName;

import id.co.veritrans.sdk.coreflow.models.TransactionDetails;

/**
 * Created by ziahaqi on 7/19/16.
 */
public class SnapTokenRequestModel {

    public SnapTokenRequestModel(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    @SerializedName("transaction_details")
    private TransactionDetails transactionDetails;
}
