package id.co.veritrans.sdk.callbacks;

import id.co.veritrans.sdk.models.CardResponse;
import id.co.veritrans.sdk.models.GetOffersResponseModel;

/**
 * Created by Ankit on 12/10/15.
 */
public interface GetOffersCallback {
    public void onSuccess(GetOffersResponseModel getOffersResponseModel);
    public void onFailure(String errorMessage, GetOffersResponseModel getOffersResponseModel);
}