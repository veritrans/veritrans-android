package com.midtrans.sdk.corekit.callback;

import com.midtrans.sdk.corekit.models.CardRegistrationResponse;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface CardRegistrationCallback extends HttpRequestCallback{

    void onSuccess(CardRegistrationResponse response);

    void onFailure(CardRegistrationResponse response, String reason);
}
