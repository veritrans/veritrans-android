package id.co.veritrans.sdk.coreflow.callback;

import id.co.veritrans.sdk.coreflow.callback.exception.TransactionFailure;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface TransactionCallback {

    public void onSuccess(TransactionResponse response);

    public void onFailure(TransactionResponse response, String reason);

    public void onError(Throwable error);
}