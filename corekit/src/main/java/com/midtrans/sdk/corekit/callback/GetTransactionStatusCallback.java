package com.midtrans.sdk.corekit.callback;


import com.midtrans.sdk.corekit.models.snap.TransactionStatusResponse;

/**
 * Created by ziahaqi on 7/29/17.
 */

public interface GetTransactionStatusCallback extends HttpRequestCallback{

    void onSuccess(TransactionStatusResponse response);

    void onFailure(TransactionStatusResponse response, String reason);
}
