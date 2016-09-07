package com.midtrans.sdk.coreflow.callback;

import com.midtrans.sdk.coreflow.models.SaveCardResponse;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface SaveCardCallback {

    public void onSuccess(SaveCardResponse response);

    public void onFailure(String reason);

    public void onError(Throwable error);
}
