package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.TransactionResponse;

/**
 * Created by rakawm on 6/8/16.
 */
public class TransactionFinishedEvent {

    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_FAILED = "failed";

    private TransactionResponse response;
    private String source;
    private String status;

    public TransactionFinishedEvent(TransactionResponse response) {
        setResponse(response);
    }

    public TransactionFinishedEvent() {

    }

    public TransactionFinishedEvent(TransactionResponse response, String source, String status) {
        setResponse(response);
        setSource(source);
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
