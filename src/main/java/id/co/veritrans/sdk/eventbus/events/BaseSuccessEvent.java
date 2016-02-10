package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.TokenDetailsResponse;

/**
 * Base VeritransBus Event class.
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
