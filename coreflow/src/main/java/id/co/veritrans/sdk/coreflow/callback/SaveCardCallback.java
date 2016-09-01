package id.co.veritrans.sdk.coreflow.callback;

import id.co.veritrans.sdk.coreflow.callback.exception.BaseError;
import id.co.veritrans.sdk.coreflow.models.SaveCardResponse;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface SaveCardCallback {

    public void onSuccess(SaveCardResponse response);

    public void onError(Throwable error);
}
