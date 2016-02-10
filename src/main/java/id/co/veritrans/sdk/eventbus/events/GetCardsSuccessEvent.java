package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.CardResponse;

/**
 * @author rakawm
 */
public class GetCardsSuccessEvent extends BaseSuccessEvent<CardResponse> {
    public GetCardsSuccessEvent(CardResponse response) {
        super(response);
    }
}
