package id.co.veritrans.sdk.eventbus.callback;

import id.co.veritrans.sdk.eventbus.events.DeleteCardFailedEvent;
import id.co.veritrans.sdk.eventbus.events.DeleteCardSuccessEvent;

/**
 * @author rakawm
 */
public interface DeleteCardBusCallback extends BaseBusCallback {
    void onEvent(DeleteCardSuccessEvent event);

    void onEvent(DeleteCardFailedEvent event);
}
