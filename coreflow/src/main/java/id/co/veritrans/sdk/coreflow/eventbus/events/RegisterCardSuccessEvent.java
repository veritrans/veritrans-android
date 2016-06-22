package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.RegisterCardResponse;

/**
 * @author rakawm
 */
public class RegisterCardSuccessEvent extends BaseSuccessEvent<RegisterCardResponse> {
    public RegisterCardSuccessEvent(RegisterCardResponse response) {
        super(response);
    }

    public RegisterCardSuccessEvent(RegisterCardResponse response, String source) {
        super(response, source);
    }
}
