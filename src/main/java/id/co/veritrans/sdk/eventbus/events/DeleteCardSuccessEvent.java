package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.DeleteCardResponse;

/**
 * @author rakawm
 */
public class DeleteCardSuccessEvent extends BaseSuccessEvent<DeleteCardResponse> {
    public DeleteCardSuccessEvent(DeleteCardResponse response) {
        super(response);
    }
}
