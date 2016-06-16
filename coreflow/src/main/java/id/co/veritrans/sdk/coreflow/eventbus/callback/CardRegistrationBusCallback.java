package id.co.veritrans.sdk.coreflow.eventbus.callback;

import id.co.veritrans.sdk.coreflow.eventbus.events.CardRegistrationFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.CardRegistrationSuccessEvent;

/**
 * @author rakawm
 */
public interface CardRegistrationBusCallback extends BaseBusCallback {
    void onEvent(CardRegistrationSuccessEvent event);

    void onEvent(CardRegistrationFailedEvent event);

}
