package id.co.veritrans.sdk.coreflow.eventbus.events;

/**
 * Created by rakawm on 2/10/16.
 */
public class GeneralErrorEvent {
    private String message;
    private String source;

    public GeneralErrorEvent(String message) {
        setMessage(message);
    }

    public GeneralErrorEvent(String message, String source) {
        setMessage(message);
        setSource(source);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
