package id.co.veritrans.sdk.coreflow.callback.exception;

import id.co.veritrans.sdk.coreflow.models.CardRegistrationResponse;

/**
 * Created by ziahaqi on 8/31/16.
 */
public class CardRegistrationError extends BaseError {
    private CardRegistrationResponse response;

    public CardRegistrationError(CardRegistrationResponse response, String detailMessage, String errorType) {
        super(detailMessage, errorType);
        this.response = response;
    }

    public CardRegistrationError(String detailMessage, String errorType) {
        super(detailMessage, errorType);
    }
}
