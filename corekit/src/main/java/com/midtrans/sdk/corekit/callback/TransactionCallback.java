package com.midtrans.sdk.corekit.callback;

import com.midtrans.sdk.corekit.models.TransactionResponse;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface TransactionCallback extends HttpRequestCallback {

    void onSuccess(TransactionResponse response);

    void onFailure(TransactionResponse response, String reason);
}