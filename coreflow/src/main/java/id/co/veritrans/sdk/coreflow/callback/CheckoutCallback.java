package id.co.veritrans.sdk.coreflow.callback;

import id.co.veritrans.sdk.coreflow.models.snap.Token;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface CheckoutCallback {

    public void onSuccess(Token token);

    public void onFailure(Token token, String reason);

    public void onError(Throwable error);
}
