package id.co.veritrans.sdk.coreflow.eventbus.events.snap;

import id.co.veritrans.sdk.coreflow.eventbus.events.BaseSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.snap.Transaction;

/**
 * Created by ziahaqi on 7/18/16.
 */
public class PaymentTypesEvent extends BaseSuccessEvent<Transaction>{

    public PaymentTypesEvent(Transaction response) {
        super(response);
    }

    public PaymentTypesEvent(Transaction response, String source) {
        super(response, source);
    }
}
