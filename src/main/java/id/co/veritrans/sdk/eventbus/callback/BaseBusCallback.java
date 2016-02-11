package id.co.veritrans.sdk.eventbus.callback;

import id.co.veritrans.sdk.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.eventbus.events.NetworkUnavailableEvent;

/**
 * @author rakawm
 */
public interface BaseBusCallback {

    void onEvent(NetworkUnavailableEvent event);

    void onEvent(GeneralErrorEvent event);
}
