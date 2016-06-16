package id.co.veritrans.sdk.eventbus.events;

/**
 * Base success event class.
 * @author rakawm
 */
public abstract class BaseSuccessEvent<T> {
    private T response;
    private String source;

    public BaseSuccessEvent(T response) {
        setResponse(response);
    }

    public BaseSuccessEvent(T response, String source) {
        setResponse(response);
        setSource(source);
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
