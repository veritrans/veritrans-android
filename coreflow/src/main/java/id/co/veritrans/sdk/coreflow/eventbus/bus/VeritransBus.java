package id.co.veritrans.sdk.coreflow.eventbus.bus;

import org.greenrobot.eventbus.EventBus;

/**
 * Custom VeritransBus based on Event VeritransBus.
 * @author rakawm
 */
public class VeritransBus extends EventBus {
    private static VeritransBus bus;

    public VeritransBus(){
        super();
    }

    public static void setBus(VeritransBus bus) {
        VeritransBus.bus = bus;
    }
}
