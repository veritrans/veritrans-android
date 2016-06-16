package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.SaveCardResponse;

/**
 * @author rakawm
 */
public class SaveCardFailedEvent extends BaseFailedEvent<SaveCardResponse> {
    public SaveCardFailedEvent(String message, SaveCardResponse response) {
        super(message, response);
    }

    public SaveCardFailedEvent(String message, SaveCardResponse response, String source) {
        super(message, response, source);
    }
}
