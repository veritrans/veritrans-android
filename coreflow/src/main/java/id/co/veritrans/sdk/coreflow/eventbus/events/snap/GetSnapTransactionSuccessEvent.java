package id.co.veritrans.sdk.coreflow.eventbus.events.snap;

import id.co.veritrans.sdk.coreflow.eventbus.events.BaseSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.snap.Transaction;

/**
 * Created by ziahaqi on 7/18/16.
 */
public class GetSnapTransactionSuccessEvent extends BaseSuccessEvent<Transaction> {

    public GetSnapTransactionSuccessEvent(Transaction response) {
        super(response);
    }

    public GetSnapTransactionSuccessEvent(Transaction response, String source) {
        super(response, source);
    }
}
