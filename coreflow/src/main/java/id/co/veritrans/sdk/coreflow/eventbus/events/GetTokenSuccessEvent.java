package id.co.veritrans.sdk.coreflow.eventbus.events;

import id.co.veritrans.sdk.coreflow.models.TokenDetailsResponse;

/**
 * @author rakawm
 */
public class GetTokenSuccessEvent extends BaseSuccessEvent<TokenDetailsResponse> {
    public GetTokenSuccessEvent(TokenDetailsResponse response) {
        super(response);
    }

    public GetTokenSuccessEvent(TokenDetailsResponse response, String source) {
        super(response, source);
    }
}
