package id.co.veritrans.sdk.coreflow.eventbus.callback;

import id.co.veritrans.sdk.coreflow.eventbus.events.GetOfferFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetOfferSuccessEvent;

/**
 * @author rakawm
 */
public interface GetOfferBusCallback extends BaseBusCallback {
    void onEvent(GetOfferSuccessEvent event);

    void onEvent(GetOfferFailedEvent event);
}
