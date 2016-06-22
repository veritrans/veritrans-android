package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.SaveCardResponse;

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
