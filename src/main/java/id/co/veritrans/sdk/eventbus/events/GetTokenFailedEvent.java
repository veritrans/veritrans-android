package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.TokenDetailsResponse;

/**
 * @author rakawm
 */
public class GetTokenFailedEvent extends BaseFailedEvent<TokenDetailsResponse> {

    public GetTokenFailedEvent(String message, TokenDetailsResponse response) {
        super(message, response);
    }

    public GetTokenFailedEvent(String message, TokenDetailsResponse response, String source) {
        super(message, response, source);
    }
}
