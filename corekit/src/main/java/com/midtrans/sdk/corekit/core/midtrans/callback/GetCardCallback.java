package com.midtrans.sdk.corekit.core.midtrans.callback;

import com.midtrans.sdk.corekit.base.callback.HttpRequestCallback;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.creditcard.SaveCardRequest;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface GetCardCallback extends HttpRequestCallback {

    void onSuccess(ArrayList<SaveCardRequest> response);

    void onFailure(String reason);
}