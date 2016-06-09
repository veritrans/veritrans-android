package id.co.veritrans.sdk.eventbus.callback;

import id.co.veritrans.sdk.eventbus.events.TransactionFinishedEvent;

/**
 * @author rakawm
 */
public interface TransactionFinishedCallback {

    void onEvent(TransactionFinishedEvent event);

}
