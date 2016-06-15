package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.CardRegistrationResponse;

/**
 * @author rakawm
 */
public class CardRegistrationFailedEvent extends BaseFailedEvent<CardRegistrationResponse> {
    public CardRegistrationFailedEvent(String message, CardRegistrationResponse response) {
        super(message, response);
    }

    public CardRegistrationFailedEvent(String message, CardRegistrationResponse response, String source) {
        super(message, response, source);
    }
}
