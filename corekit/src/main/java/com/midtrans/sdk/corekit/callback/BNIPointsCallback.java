package com.midtrans.sdk.corekit.callback;

import com.midtrans.sdk.corekit.models.snap.BankPointsResponse;

/**
 * Created by ziahaqi on 1/12/17.
 */

public interface BNIPointsCallback {

    void onSuccess(BankPointsResponse response);

    void onFailure(String reason);

    void onError(Throwable error);
}
