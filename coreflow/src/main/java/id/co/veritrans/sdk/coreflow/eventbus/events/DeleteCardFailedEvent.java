package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.DeleteCardResponse;

/**
 * @author rakawm
 */
public class DeleteCardFailedEvent extends BaseFailedEvent<DeleteCardResponse> {
    public DeleteCardFailedEvent(String message, DeleteCardResponse response) {
        super(message, response);
    }
}
