package id.co.veritrans.sdk.coreflow.eventbus.callback;

import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTransactionSuccessEvent;

/**
 * Created by ziahaqi on 7/19/16.
 */
public interface GetSnapTransactionCallback {
    void onEvent(GetSnapTransactionSuccessEvent event);

    void onEvent(GetSnapTransactionFailedEvent event);
}
