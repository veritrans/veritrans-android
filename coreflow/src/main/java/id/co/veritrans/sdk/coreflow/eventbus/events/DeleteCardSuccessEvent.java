package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.DeleteCardResponse;

/**
 * @author rakawm
 */
public class DeleteCardSuccessEvent extends BaseSuccessEvent<DeleteCardResponse> {
    public DeleteCardSuccessEvent(DeleteCardResponse response) {
        super(response);
    }

    public DeleteCardSuccessEvent(DeleteCardResponse response, String source) {
        super(response, source);
    }
}
