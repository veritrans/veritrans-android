package com.midtrans.sdk.coreflow.callback;

import com.midtrans.sdk.coreflow.models.snap.TransactionResult;

/**
 * Created by ziahaqi on 9/1/16.
 */
public interface TransactionFinishedCallback {

    public void onTransactionFinished(TransactionResult result);
}
