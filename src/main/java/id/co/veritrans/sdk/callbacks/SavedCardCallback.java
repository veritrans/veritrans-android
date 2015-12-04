package id.co.veritrans.sdk.callbacks;

import id.co.veritrans.sdk.models.CardResponse;
/**
 * Created by chetan on 04/12/15.
 */
public interface SavedCardCallback {


    public void onSuccess(CardResponse cardResponse);



    public void onFailure(String errorMessage, CardResponse cardResponse);
}