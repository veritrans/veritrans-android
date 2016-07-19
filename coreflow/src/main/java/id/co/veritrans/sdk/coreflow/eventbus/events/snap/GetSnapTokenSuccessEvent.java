package id.co.veritrans.sdk.coreflow.eventbus.events.snap;

import id.co.veritrans.sdk.coreflow.eventbus.events.BaseSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.snap.Token;

/**
 * @author rakawm
 */
public class GetSnapTokenSuccessEvent extends BaseSuccessEvent<Token> {
    public GetSnapTokenSuccessEvent(Token response) {
        super(response);
    }

    public GetSnapTokenSuccessEvent(Token response, String source) {
        super(response, source);
    }
}
