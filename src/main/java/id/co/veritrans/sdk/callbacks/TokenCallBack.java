package id.co.veritrans.sdk.callbacks;

import id.co.veritrans.sdk.models.TokenDetailsResponse;

/**
 * Contains call back methods to handle get token api call back response.
 *
 * Created by shivam on 10/29/15.
 */
public interface TokenCallBack {

    /**
     * It will be called when get token api call gets successfully executed.
     * @param tokenDetailsResponse server response with toekn.
     */
    public void onSuccess(TokenDetailsResponse tokenDetailsResponse);


    /**
     * It will be called when get token api call failed to get token information.
     * @param tokenDetailsResponse server response about failure.
     */
    public void onFailure(String errorMessage, TokenDetailsResponse tokenDetailsResponse);
}