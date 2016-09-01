package id.co.veritrans.sdk.coreflow.callback.exception;

import id.co.veritrans.sdk.coreflow.models.TransactionResponse;

/**
 * Created by ziahaqi on 8/31/16.
 */
public class TransactionFailure {
    private TransactionResponse transactionResponse;
    private String errorMessage;

    public TransactionFailure(TransactionResponse transactionResponse, String errorMessage) {
        this.transactionResponse = transactionResponse;
        this.errorMessage = errorMessage;
    }

    public TransactionResponse getTransactionResponse() {
        return transactionResponse;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
