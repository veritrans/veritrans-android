package com.midtrans.sdk.corekit.callback;

import com.midtrans.sdk.corekit.models.SaveCardResponse;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface SaveCardCallback {

    void onSuccess(SaveCardResponse response);

    void onFailure(String reason);

    void onError(Throwable error);
}
