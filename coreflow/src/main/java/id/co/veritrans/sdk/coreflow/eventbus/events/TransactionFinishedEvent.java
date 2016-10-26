package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.TransactionResponse;

/**
 * Created by rakawm on 6/8/16.
 */
public class TransactionFinishedEvent {

    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_PENDING = 2;

    private TransactionResponse response;
    private String source;
    private int status;

    public TransactionFinishedEvent(TransactionResponse response) {
        setResponse(response);
    }

    public TransactionFinishedEvent(TransactionResponse response, int status) {
        setResponse(response);
        setStatus(status);
    }

    public TransactionFinishedEvent() {

    }

    public TransactionFinishedEvent(TransactionResponse response, String source) {
        setResponse(response);
        setSource(source);
    }

    public TransactionFinishedEvent(int status) {
        setStatus(status);
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
