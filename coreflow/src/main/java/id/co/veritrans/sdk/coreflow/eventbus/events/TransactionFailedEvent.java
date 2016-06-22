package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.TransactionResponse;

/**
 * @author rakawm
 */
public class TransactionFailedEvent extends BaseFailedEvent<TransactionResponse> {
    public TransactionFailedEvent(String message, TransactionResponse response) {
        super(message, response);
    }

    public TransactionFailedEvent(String message, TransactionResponse response, String source) {
        super(message, response, source);
    }
}
