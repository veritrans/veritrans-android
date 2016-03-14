package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.CardRegistrationResponse;

/**
 * @author rakawm
 */
public class CardRegistrationSuccessEvent extends BaseSuccessEvent<CardRegistrationResponse> {
    public CardRegistrationSuccessEvent(CardRegistrationResponse response) {
        super(response);
    }
}
