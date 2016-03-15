package id.co.veritrans.sdk.eventbus.callback;

import id.co.veritrans.sdk.eventbus.events.AuthenticationEvent;

/**
 * @author rakawm
 */
public interface GetAuthenticationBusCallback extends BaseBusCallback {
    void onEvent(AuthenticationEvent event);
}
