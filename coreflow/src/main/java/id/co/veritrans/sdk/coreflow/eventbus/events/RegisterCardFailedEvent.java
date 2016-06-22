package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.RegisterCardResponse;

/**
 * @author rakawm
 */
public class RegisterCardFailedEvent extends BaseFailedEvent<RegisterCardResponse> {
    public RegisterCardFailedEvent(String message, RegisterCardResponse response) {
        super(message, response);
    }

    public RegisterCardFailedEvent(String message, RegisterCardResponse response, String source) {
        super(message, response, source);
    }
}
