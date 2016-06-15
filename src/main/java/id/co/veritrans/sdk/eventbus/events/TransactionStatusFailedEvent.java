package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.TransactionStatusResponse;

/**
 * @author rakawm
 */
public class TransactionStatusFailedEvent extends BaseFailedEvent<TransactionStatusResponse> {
    public TransactionStatusFailedEvent(String message, TransactionStatusResponse response) {
        super(message, response);
    }

    public TransactionStatusFailedEvent(String message, TransactionStatusResponse response, String source) {
        super(message, response, source);
    }
}
