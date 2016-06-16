package id.co.veritrans.sdk.coreflow.eventbus.callback;

import id.co.veritrans.sdk.coreflow.eventbus.events.SaveCardFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SaveCardSuccessEvent;

/**
 * @author rakawm
 */
public interface SaveCardBusCallback extends BaseBusCallback {
    void onEvent(SaveCardSuccessEvent event);

    void onEvent(SaveCardFailedEvent event);
}
