package id.co.veritrans.sdk.coreflow.callback.exception;

import id.co.veritrans.sdk.coreflow.models.TokenDetailsResponse;

/**
 * Created by ziahaqi on 8/31/16.
 */
public class GetCardTokenError extends BaseError {
    private TokenDetailsResponse response;

    public GetCardTokenError(TokenDetailsResponse response, String detailMessage, String errorType) {
        super(detailMessage, errorType);
        this.response = response;
    }

    public GetCardTokenError(String detailMessage, String errorType) {
        super(detailMessage, errorType);
    }
}
