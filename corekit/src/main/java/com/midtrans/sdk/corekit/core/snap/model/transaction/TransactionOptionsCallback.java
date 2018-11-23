package com.midtrans.sdk.corekit.core.snap.model.transaction;

import com.midtrans.sdk.corekit.base.callback.HttpRequestCallback;
import com.midtrans.sdk.corekit.core.snap.model.transaction.response.PaymentInfoResponse;

public interface TransactionOptionsCallback extends HttpRequestCallback {

    void onSuccess(PaymentInfoResponse transaction);

    void onFailure(PaymentInfoResponse transaction, String reason);
}