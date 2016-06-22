package id.co.veritrans.sdk.coreflow.eventbus.callback;

import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionSuccessEvent;

/**
 * @author rakawm
 */
public interface TransactionBusCallback extends BaseBusCallback {

    void onEvent(TransactionSuccessEvent event);

    void onEvent(TransactionFailedEvent event);
}
