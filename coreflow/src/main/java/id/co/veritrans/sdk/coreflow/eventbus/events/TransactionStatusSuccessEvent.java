package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.TransactionStatusResponse;

/**
 * @author rakawm
 */
public class TransactionStatusSuccessEvent extends BaseSuccessEvent<TransactionStatusResponse> {
    public TransactionStatusSuccessEvent(TransactionStatusResponse response) {
        super(response);
    }

    public TransactionStatusSuccessEvent(TransactionStatusResponse response, String source) {
        super(response, source);
    }
}
