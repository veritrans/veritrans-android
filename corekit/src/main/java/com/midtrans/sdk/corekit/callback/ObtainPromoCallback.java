package com.midtrans.sdk.corekit.callback;

import com.midtrans.sdk.corekit.models.promo.PromosResponse;

/**
 * Created by rakawm on 2/3/17.
 */

public interface ObtainPromoCallback {
    void onSuccess(PromosResponse response);

    void onFailure(String statusCode, String message);

    void onError(Throwable throwable);
}
