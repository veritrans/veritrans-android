package id.co.veritrans.sdk.coreflow.callback;

import id.co.veritrans.sdk.coreflow.models.TokenDetailsResponse;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface GetCardTokenCallback {

    public void onSuccess(TokenDetailsResponse response);

    public void onFailure(TokenDetailsResponse response, String reason);

    public void onError(Throwable error);
}
