package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.AuthModel;

/**
 * @author rakawm
 */
public class AuthenticationEvent extends BaseSuccessEvent<AuthModel> {

    public AuthenticationEvent(AuthModel response) {
        super(response);
    }
}
