package id.co.veritrans.sdk.coreflow.eventbus.callback;

import id.co.veritrans.sdk.coreflow.core.MerchantRestAPI;
import id.co.veritrans.sdk.coreflow.eventbus.events.CardRegistrationFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.CardRegistrationSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.KlikBCAModel;

/**
 * @author rakawm
 */
public interface CardRegistrationBusCallback extends BaseBusCallback {
    void onEvent(CardRegistrationSuccessEvent event);

    void onEvent(CardRegistrationFailedEvent event);
}
