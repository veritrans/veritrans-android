package id.co.veritrans.sdk.coreflow.eventbus.events.snap;

import id.co.veritrans.sdk.coreflow.eventbus.events.BaseSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.snap.Transaction;

/**
 * Created by ziahaqi on 7/18/16.
 */
public class GetPaymentListSuccessEvent extends BaseSuccessEvent<Transaction>{

    public GetPaymentListSuccessEvent(Transaction response) {
        super(response);
    }

    public GetPaymentListSuccessEvent(Transaction response, String source) {
        super(response, source);
    }
}
