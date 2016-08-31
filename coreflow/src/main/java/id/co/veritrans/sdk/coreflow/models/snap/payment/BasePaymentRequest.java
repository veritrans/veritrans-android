package id.co.veritrans.sdk.coreflow.models.snap.payment;

import com.google.gson.annotations.SerializedName;

/**
 * @author rakawm
 */
public class BasePaymentRequest {
    @SerializedName("transaction_id")
    protected String transactionId;
    protected String authenticityToken;

    public BasePaymentRequest() {

    }

    public BasePaymentRequest(String transactionId) {
        setTransactionId(transactionId);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAuthenticityToken() {
        return authenticityToken;
    }

    public void setAuthenticityToken(String authenticityToken) {
        this.authenticityToken = authenticityToken;
    }
}
