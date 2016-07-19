package id.co.veritrans.sdk.coreflow.eventbus.callback;

import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetPaymentListFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetPaymentListSuccessEvent;

/**
 * Created by ziahaqi on 7/19/16.
 */
public interface GetPaymentListCallback {
    void onEvent(GetPaymentListSuccessEvent event);
    void onEvent(GetPaymentListFailedEvent event);
}
