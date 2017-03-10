package com.midtrans.sdk.corekit.callback;

import com.midtrans.sdk.corekit.models.snap.BanksPointResponse;

/**
 * Created by ziahaqi on 1/12/17.
 */

public interface BanksPointCallback {

    void onSuccess(BanksPointResponse response);

    void onFailure(String reason);

    void onError(Throwable error);
}
