package id.co.veritrans.sdk.coreflow.eventbus.callback.snap;

import id.co.veritrans.sdk.coreflow.eventbus.callback.BaseBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetCardsFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetCardsSuccessEvent;

/**
 * Created by ziahaqi on 8/06/16.
 */
public interface GetCardsBusCallback extends BaseBusCallback {
    void onEvent(GetCardsSuccessEvent event);

    void onEvent(GetCardsFailedEvent event);
}
