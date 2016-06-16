package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.TokenDetailsResponse;

/**
 * @author rakawm
 */
public class GetTokenFailedEvent extends BaseFailedEvent<TokenDetailsResponse> {

    public GetTokenFailedEvent(String message, TokenDetailsResponse response) {
        super(message, response);
    }
}
