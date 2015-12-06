package id.co.veritrans.sdk.callbacks;

import id.co.veritrans.sdk.models.TransactionResponse;

/**
 *  Contains call back methods to handle payment api call back response.
 *
 * Created by shivam on 10/29/15.
 */
public interface TransactionCallback {

    /**
     * It will be called when payment api call request gets executed successfully on server.
     * To get payment status information use TransactionResponse instance.
     *
     * @param transactionResponse payment status information.
     */
    public void onSuccess(TransactionResponse transactionResponse);

    /**
     * It will be called when payment transaction gets failed.
     *
     * @param errorMessage error message returned by the server.
     * @param transactionResponse payment status information, it can be null.
     */
    public void onFailure(String errorMessage, TransactionResponse transactionResponse);
}