package id.co.veritrans.sdk.coreflow.eventbus.callback;

import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFinishedEvent;

/**
 * @author rakawm
 */
public interface TransactionFinishedCallback {

    void onEvent(TransactionFinishedEvent event);

}
