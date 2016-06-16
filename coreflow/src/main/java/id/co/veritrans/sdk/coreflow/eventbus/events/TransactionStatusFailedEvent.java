package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.TransactionStatusResponse;

/**
 * @author rakawm
 */
public class TransactionStatusFailedEvent extends BaseFailedEvent<TransactionStatusResponse> {
    public TransactionStatusFailedEvent(String message, TransactionStatusResponse response) {
        super(message, response);
    }
}
