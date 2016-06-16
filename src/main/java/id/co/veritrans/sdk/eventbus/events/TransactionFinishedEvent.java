package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.TransactionResponse;

/**
 * Created by rakawm on 6/8/16.
 */
public class TransactionFinishedEvent {

    private TransactionResponse response;
    private String source;

    public TransactionFinishedEvent(TransactionResponse response) {
        setResponse(response);
    }

    public TransactionFinishedEvent() {

    }

    public TransactionFinishedEvent(TransactionResponse response, String source) {
        setResponse(response);
        setSource(source);
    }

    public TransactionResponse getResponse() {
        return response;
    }

    public void setResponse(TransactionResponse response) {
        this.response = response;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
