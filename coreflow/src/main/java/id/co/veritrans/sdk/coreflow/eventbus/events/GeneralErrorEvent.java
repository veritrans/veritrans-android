package id.co.veritrans.sdk.coreflow.eventbus.events;

/**
 * Created by rakawm on 2/10/16.
 */
public class GeneralErrorEvent {
    private String message;

    public GeneralErrorEvent(String message) {
        setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
