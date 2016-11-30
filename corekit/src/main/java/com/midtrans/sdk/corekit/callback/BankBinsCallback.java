package com.midtrans.sdk.corekit.callback;

import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 11/28/16.
 */

public interface BankBinsCallback {

    void onSuccess(ArrayList<BankBinsResponse> response);

    void onFailure(String reason);

    void onError(Throwable error);

}
