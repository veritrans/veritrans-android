package com.midtrans.sdk.corekit.callback;

import com.midtrans.sdk.corekit.models.snap.TransactionResult;

/**
 * Created by ziahaqi on 9/1/16.
 */
public interface TransactionFinishedCallback {

    void onTransactionFinished(TransactionResult result);
}
