package id.co.veritrans.sdk.coreflow.eventbus.events;

/**
 * @author rakawm
 */
public class SSLErrorEvent {
    private String source;

    public SSLErrorEvent() {

    }

    public SSLErrorEvent(String source) {
        setSource(source);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
