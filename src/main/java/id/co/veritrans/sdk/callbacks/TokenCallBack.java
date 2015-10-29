package id.co.veritrans.sdk.callbacks;

import id.co.veritrans.sdk.models.TokenDetailsResponse;

/**
 * Created by shivam on 10/29/15.
 */
public interface TokenCallBack extends Callback {
    public void onSuccess(TokenDetailsResponse tokenDetailsResponse);
}