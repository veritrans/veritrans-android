package id.co.veritrans.sdk.coreflow.core;

import com.google.gson.annotations.SerializedName;

/**
 * @author rakawm
 */
public class SnapTransactionRequest {
    @SerializedName("transaction_details")
    private SnapTransactionDetails transactionDetails;

    public SnapTransactionRequest(SnapTransactionDetails details) {
        setTransactionDetails(details);
    }

    public SnapTransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(SnapTransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }
}
