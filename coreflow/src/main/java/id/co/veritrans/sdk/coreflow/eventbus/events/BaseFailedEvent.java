package id.co.veritrans.sdk.coreflow.eventbus.events;

/**
 * Base failed event class.
 * @author rakawm
 */
public abstract class BaseFailedEvent<T> {
    private String message;
    private T response;

    public BaseFailedEvent(String message, T response) {
        setMessage(message);
        setResponse(response);
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
}
