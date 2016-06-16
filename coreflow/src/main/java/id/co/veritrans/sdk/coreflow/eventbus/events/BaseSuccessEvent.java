package id.co.veritrans.sdk.coreflow.eventbus.events;

/**
 * Base success event class.
 * @author rakawm
 */
public abstract class BaseSuccessEvent<T> {
    private T response;

    public BaseSuccessEvent(T response) {
        setResponse(response);
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
