package id.co.veritrans.sdk.coreflow.eventbus.callback;

import id.co.veritrans.sdk.coreflow.eventbus.events.SSLErrorEvent;

/**
 * Created by ziahaqi on 30/06/2016.
 */
public interface HttpErrorCallback {

    void onEvent(SSLErrorEvent errorEvent);
}
