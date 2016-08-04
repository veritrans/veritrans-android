package id.co.veritrans.sdk.coreflow.eventbus.events.snap;

import id.co.veritrans.sdk.coreflow.eventbus.events.BaseFailedEvent;
import id.co.veritrans.sdk.coreflow.models.snap.Transaction;

/**
 * Created by ziahaqi on 7/19/16.
 */
public class GetPaymentListFailedEvent extends BaseFailedEvent<Transaction>{

    public GetPaymentListFailedEvent(String message, Transaction response) {
        super(message, response);
    }

    public GetPaymentListFailedEvent(String message, Transaction response, String source) {
        super(message, response, source);
    }
}
