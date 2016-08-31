package id.co.veritrans.sdk.coreflow.eventbus.events.snap;

import id.co.veritrans.sdk.coreflow.eventbus.events.BaseFailedEvent;
import id.co.veritrans.sdk.coreflow.models.snap.Token;

/**
 * @author rakawm
 */
public class GetSnapTokenFailedEvent extends BaseFailedEvent<Token> {

    public GetSnapTokenFailedEvent(String message, Token response) {
        super(message, response);
    }

    public GetSnapTokenFailedEvent(String message, Token response, String source) {
        super(message, response, source);
    }
}
