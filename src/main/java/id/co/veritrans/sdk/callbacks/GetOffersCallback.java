package id.co.veritrans.sdk.callbacks;

import id.co.veritrans.sdk.models.GetOffersResponseModel;

/**
 * Created by Ankit on 12/10/15.
 */
public interface GetOffersCallback {
    void onSuccess(GetOffersResponseModel getOffersResponseModel);

    void onFailure(String errorMessage, GetOffersResponseModel getOffersResponseModel);
}