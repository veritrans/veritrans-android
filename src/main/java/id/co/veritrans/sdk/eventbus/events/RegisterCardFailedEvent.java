package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.RegisterCardResponse;

/**
 * @author rakawm
 */
public class RegisterCardFailedEvent extends BaseFailedEvent<RegisterCardResponse> {
    public RegisterCardFailedEvent(String message, RegisterCardResponse response) {
        super(message, response);
    }
}
