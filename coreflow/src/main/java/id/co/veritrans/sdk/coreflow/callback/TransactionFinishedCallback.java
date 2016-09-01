package id.co.veritrans.sdk.coreflow.callback;

import id.co.veritrans.sdk.coreflow.models.snap.TransactionResult;

/**
 * Created by ziahaqi on 9/1/16.
 */
public interface TransactionFinishedCallback {

    public void onFinished(TransactionResult result);
}
