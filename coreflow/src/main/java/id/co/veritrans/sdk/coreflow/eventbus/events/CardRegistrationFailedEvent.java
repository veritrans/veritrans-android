package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.CardRegistrationResponse;

/**
 * @author rakawm
 */
public class CardRegistrationFailedEvent extends BaseFailedEvent<CardRegistrationResponse> {
    public CardRegistrationFailedEvent(String message, CardRegistrationResponse response) {
        super(message, response);
    }
}
