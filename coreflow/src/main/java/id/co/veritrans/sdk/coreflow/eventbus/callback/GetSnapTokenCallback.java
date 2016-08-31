package id.co.veritrans.sdk.coreflow.eventbus.callback;

import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTokenFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTokenSuccessEvent;

/**
 * Created by ziahaqi on 7/19/16.
 */
public interface GetSnapTokenCallback {

    void onEvent(GetSnapTokenSuccessEvent event);
    void onEvent(GetSnapTokenFailedEvent event);

}
