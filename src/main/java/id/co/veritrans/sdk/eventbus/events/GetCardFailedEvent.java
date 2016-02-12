package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.CardResponse;

/**
 * @author rakawm
 */
public class GetCardFailedEvent extends BaseFailedEvent<CardResponse> {
    public GetCardFailedEvent(String message, CardResponse response) {
        super(message, response);
    }
}
