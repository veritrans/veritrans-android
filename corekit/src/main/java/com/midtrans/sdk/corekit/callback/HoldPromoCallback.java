package com.midtrans.sdk.corekit.callback;

import com.midtrans.sdk.corekit.models.promo.HoldPromoResponse;

/**
 * Created by ziahaqi on 12/27/17.
 */

public interface HoldPromoCallback {

    void onSuccess(HoldPromoResponse response);

    void onFailure(String message);

    void onError(Throwable throwable);
}
