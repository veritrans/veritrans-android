package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.CardResponse;

/**
 * @author rakawm
 */
public class SaveCardFailedEvent extends BaseFailedEvent<CardResponse> {
    public SaveCardFailedEvent(String message, CardResponse response) {
        super(message, response);
    }
}
