package id.co.veritrans.sdk.coreflow.callback;

import id.co.veritrans.sdk.coreflow.models.CardRegistrationResponse;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface CardRegistrationCallback {

    public void onSuccess(CardRegistrationResponse response);

    public void onFailure(CardRegistrationResponse response, String reason);

    public void onError(Throwable error);
}
