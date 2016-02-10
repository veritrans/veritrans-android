package id.co.veritrans.sdk.eventbus.bus;

/**
 * @author rakawm
 */
public class VeritransBusProvider {
    private static VeritransBus instance = new VeritransBus();

    public static VeritransBus getInstance() {
        return instance;
    }

    private VeritransBusProvider() {
    }
}
