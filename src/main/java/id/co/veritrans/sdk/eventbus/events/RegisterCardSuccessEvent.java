package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.RegisterCardResponse;

/**
 * Bust event for register card success.
 * @author rakawm
 */
public class RegisterCardSuccessEvent extends BaseSuccessEvent<RegisterCardResponse> {
    public RegisterCardSuccessEvent(RegisterCardResponse response) {
        super(response);
    }
}
