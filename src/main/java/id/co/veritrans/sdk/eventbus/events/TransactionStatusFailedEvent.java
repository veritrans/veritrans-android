package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.TransactionStatusResponse;

/**
 * Created by rakawm on 2/10/16.
 */
public class TransactionStatusFailedEvent extends BaseFailedEvent<TransactionStatusResponse> {
    public TransactionStatusFailedEvent(String message, TransactionStatusResponse response) {
        super(message, response);
    }
}
