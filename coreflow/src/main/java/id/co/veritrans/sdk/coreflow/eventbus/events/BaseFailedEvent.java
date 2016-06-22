package id.co.veritrans.sdk.coreflow.eventbus.events;

/**
 * Base failed event class.
 * @author rakawm
 */
public abstract class BaseFailedEvent<T> {
    private String source;
    private String message;
    private T response;

    public BaseFailedEvent(String message, T response) {
        setMessage(message);
        setResponse(response);
    }

    public BaseFailedEvent(String message, T response, String source) {
        setMessage(message);
        setResponse(response);
        setSource(source);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
