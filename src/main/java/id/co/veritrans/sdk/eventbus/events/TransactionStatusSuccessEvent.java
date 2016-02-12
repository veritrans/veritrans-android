package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.TransactionStatusResponse;

/**
 * @author rakawm
 */
public class TransactionStatusSuccessEvent extends BaseSuccessEvent<TransactionStatusResponse> {
    public TransactionStatusSuccessEvent(TransactionStatusResponse response) {
        super(response);
    }
}
