package id.co.veritrans.sdk.eventbus.callback;

import id.co.veritrans.sdk.eventbus.events.CardRegistrationFailedEvent;
import id.co.veritrans.sdk.eventbus.events.CardRegistrationSuccessEvent;

/**
 * @author rakawm
 */
public interface CardRegistrationBusCallback extends BaseBusCallback {
    void onEvent(CardRegistrationSuccessEvent event);

    void onEvent(CardRegistrationFailedEvent event);

}
