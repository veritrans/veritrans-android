package id.co.veritrans.sdk.coreflow.callback.exception;

import id.co.veritrans.sdk.coreflow.models.snap.Token;

/**
 * Created by ziahaqi on 8/31/16.
 */
public class CheckoutError extends BaseError {
    private Token token;

    public CheckoutError(Token token, String detailMessage, String errorType) {
        super(detailMessage, errorType);
        this.token = token;
    }

    public CheckoutError(String detailMessage, String errorType) {
        super(detailMessage, errorType);
    }

    public Token getToken() {
        return token;
    }
}
