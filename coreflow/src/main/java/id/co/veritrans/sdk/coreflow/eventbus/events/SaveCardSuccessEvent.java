package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.SaveCardResponse;

/**
 * @author rakawm
 */
public class SaveCardSuccessEvent extends BaseSuccessEvent<SaveCardResponse> {
    public SaveCardSuccessEvent(SaveCardResponse response) {
        super(response);
    }
}
