package id.co.veritrans.sdk.coreflow.callback;

import java.util.ArrayList;

import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;

/**
 * Created by ziahaqi on 8/31/16.
 */
public interface GetCardCallback {

    public void onSuccess(ArrayList<SaveCardRequest> response);

    public void onFailure(String reason);

    public void onError(Throwable error);
}
