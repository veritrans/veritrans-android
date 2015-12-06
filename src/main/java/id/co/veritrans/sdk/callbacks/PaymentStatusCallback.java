package id.co.veritrans.sdk.callbacks;

import id.co.veritrans.sdk.models.TransactionStatusResponse;

public interface PaymentStatusCallback {
    public void onFailure(String errorMessage, TransactionStatusResponse transactionStatusResponse);

    public void onSuccess(TransactionStatusResponse transactionStatusResponse);
}