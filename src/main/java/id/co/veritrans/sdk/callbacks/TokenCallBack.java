package id.co.veritrans.sdk.callbacks;

import id.co.veritrans.sdk.models.TokenDetailsResponse;

/**
 * Created by shivam on 10/29/15.
 */
public interface TokenCallBack extends TransactionCallback {
    public void onSuccess(TokenDetailsResponse tokenDetailsResponse);
}