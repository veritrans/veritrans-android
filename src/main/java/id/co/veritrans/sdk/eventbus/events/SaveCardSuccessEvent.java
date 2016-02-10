package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.CardResponse;

/**
 * @author rakawm
 */
public class SaveCardSuccessEvent extends BaseSuccessEvent<CardResponse> {
    public SaveCardSuccessEvent(CardResponse response) {
        super(response);
    }
}
