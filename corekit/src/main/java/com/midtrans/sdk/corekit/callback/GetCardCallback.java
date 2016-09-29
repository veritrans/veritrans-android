package com.midtrans.sdk.corekit.callback;

import com.midtrans.sdk.corekit.models.SaveCardRequest;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface GetCardCallback {

    void onSuccess(ArrayList<SaveCardRequest> response);

    void onFailure(String reason);

    void onError(Throwable error);
}
