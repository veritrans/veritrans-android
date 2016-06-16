package id.co.veritrans.sdk.coreflow.eventbus.callback;

import id.co.veritrans.sdk.coreflow.eventbus.events.GetTokenFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetTokenSuccessEvent;

/**
 * @author rakawm
 */
public interface TokenBusCallback extends BaseBusCallback {
    void onEvent(GetTokenSuccessEvent event);

    void onEvent(GetTokenFailedEvent event);
}
