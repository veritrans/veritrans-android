package id.co.veritrans.sdk.coreflow.eventbus.events.snap;

import id.co.veritrans.sdk.coreflow.eventbus.events.BaseFailedEvent;
import id.co.veritrans.sdk.coreflow.models.snap.Transaction;

/**
 * @author rakawm
 */
public class GetSnapTransactionFailedEvent extends BaseFailedEvent<Transaction> {

    public GetSnapTransactionFailedEvent(String message, Transaction response) {
        super(message, response);
    }

    public GetSnapTransactionFailedEvent(String message, Transaction response, String source) {
        super(message, response, source);
    }
}
