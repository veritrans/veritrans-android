package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.AuthModel;

/**
 * @author rakawm
 */
public class AuthenticationEvent extends BaseSuccessEvent<AuthModel> {

    public AuthenticationEvent(AuthModel response) {
        super(response);
    }
}
