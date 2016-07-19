package id.co.veritrans.sdk.coreflow.core;

import id.co.veritrans.sdk.coreflow.models.snap.Transaction;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * @author rakawm
 */
public interface SnapRestAPI {

    /*
     * Get snap transaction details using Snap Endpoint.
     */
    @GET("/v1/payment_pages/{snap_token}")
    void getSnapTransaction(@Path("snap_token") String snapToken, Callback<Transaction> transactionCallback);
}
