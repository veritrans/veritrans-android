package id.co.veritrans.sdk.callbacks;

import id.co.veritrans.sdk.models.DeleteCardResponse;

/**
 * Created by chetan on 04/12/15.
 */
public interface DeleteCardCallback {
    public void onFailure(String errorMessage);

    public void onSuccess(DeleteCardResponse deleteResponse);
}
