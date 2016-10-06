package com.midtrans.sdk.corekit.callback;

import com.midtrans.sdk.corekit.models.snap.Transaction;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface TransactionOptionsCallback {

    void onSuccess(Transaction transaction);

    void onFailure(Transaction transaction, String reason);

    void onError(Throwable error);

}
