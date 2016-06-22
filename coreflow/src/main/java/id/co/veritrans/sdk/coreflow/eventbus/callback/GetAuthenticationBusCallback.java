package id.co.veritrans.sdk.coreflow.eventbus.callback;

import id.co.veritrans.sdk.coreflow.eventbus.events.AuthenticationEvent;

/**
 * @author rakawm
 */
public interface GetAuthenticationBusCallback extends BaseBusCallback {
    void onEvent(AuthenticationEvent event);
}
