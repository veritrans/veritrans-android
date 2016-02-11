package id.co.veritrans.sdk.eventbus.callback;

import id.co.veritrans.sdk.eventbus.events.GetTokenFailedEvent;
import id.co.veritrans.sdk.eventbus.events.GetTokenSuccessEvent;

/**
 * @author rakawm
 */
public interface TokenBusCallback extends BaseBusCallback {
    void onEvent(GetTokenSuccessEvent event);

    void onEvent(GetTokenFailedEvent event);
}
