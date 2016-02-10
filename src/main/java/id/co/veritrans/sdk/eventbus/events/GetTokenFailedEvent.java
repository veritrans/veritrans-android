package id.co.veritrans.sdk.eventbus.events;

import id.co.veritrans.sdk.models.TokenDetailsResponse;

/**
 * Created by rakawm on 2/10/16.
 */
public class GetTokenFailedEvent extends BaseFailedEvent<TokenDetailsResponse> {

    public GetTokenFailedEvent(String message, TokenDetailsResponse response) {
        super(message, response);
    }
}
