package com.midtrans.sdk.corekit.core.snap.model.transaction;

import com.midtrans.sdk.corekit.base.callback.HttpRequestCallback;
import com.midtrans.sdk.corekit.core.snap.model.transaction.response.TransactionOptionsResponse;

public interface TransactionOptionsCallback extends HttpRequestCallback {

    void onSuccess(TransactionOptionsResponse transaction);

    void onFailure(TransactionOptionsResponse transaction, String reason);
}