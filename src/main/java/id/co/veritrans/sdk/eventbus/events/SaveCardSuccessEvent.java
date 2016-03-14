package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.SaveCardResponse;

/**
 * @author rakawm
 */
public class SaveCardSuccessEvent extends BaseSuccessEvent<SaveCardResponse> {
    public SaveCardSuccessEvent(SaveCardResponse response) {
        super(response);
    }
}
