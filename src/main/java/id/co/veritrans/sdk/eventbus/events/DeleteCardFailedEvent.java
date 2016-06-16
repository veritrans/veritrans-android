package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.DeleteCardResponse;

/**
 * @author rakawm
 */
public class DeleteCardFailedEvent extends BaseFailedEvent<DeleteCardResponse> {
    public DeleteCardFailedEvent(String message, DeleteCardResponse response) {
        super(message, response);
    }

    public DeleteCardFailedEvent(String message, DeleteCardResponse response, String source) {
        super(message, response, source);
    }
}
