package id.co.veritrans.sdk.eventbus.events;

/**
 * @author rakawm
 */
public class NetworkUnavailableEvent {
    private String source;

    public NetworkUnavailableEvent() {

    }

    public NetworkUnavailableEvent(String source) {
        setSource(source);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
