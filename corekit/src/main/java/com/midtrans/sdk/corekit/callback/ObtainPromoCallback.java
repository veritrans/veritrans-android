package com.midtrans.sdk.corekit.callback;

import com.midtrans.sdk.corekit.models.promo.ObtainPromoResponse;

/**
 * Created by rakawm on 2/3/17.
 */

public interface ObtainPromoCallback {
    void onSuccess(ObtainPromoResponse response);

    void onFailure(String message);

    void onError(Throwable throwable);
}
