package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.TransactionResponse;

/**
 * Created by rakawm on 6/8/16.
 */
public class TransactionFinishedEvent {

    private TransactionResponse response;

    public TransactionFinishedEvent(TransactionResponse response) {
        setResponse(response);
    }

    public TransactionFinishedEvent() {

    }

    public TransactionResponse getResponse() {
        return response;
    }

    public void setResponse(TransactionResponse response) {
        this.response = response;
    }
}
