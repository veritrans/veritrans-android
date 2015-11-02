package id.co.veritrans.sdk.callbacks;

import id.co.veritrans.sdk.models.TransactionResponse;

/**
 * Created by shivam on 10/29/15.
 */
public interface TransactionCallback {
    public void onFailure(String errorMessage);
    public void onSuccess(TransactionResponse transactionResponse);
}