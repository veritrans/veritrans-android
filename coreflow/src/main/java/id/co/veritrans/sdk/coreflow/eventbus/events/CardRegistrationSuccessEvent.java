package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.CardRegistrationResponse;

/**
 * @author rakawm
 */
public class CardRegistrationSuccessEvent extends BaseSuccessEvent<CardRegistrationResponse> {
    public CardRegistrationSuccessEvent(CardRegistrationResponse response) {
        super(response);
    }

    public CardRegistrationSuccessEvent(CardRegistrationResponse response, String source) {
        super(response, source);
    }
}
