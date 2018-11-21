package com.midtrans.sdk.corekit.core.snap;

import com.midtrans.sdk.corekit.core.snap.model.transaction.response.TransactionOptionsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SnapApiService {

    /**
     * Get transaction options using Snap with snap token.
     *
     * @param snapToken snap token
     */
    @GET("v1/transactions/{snap_token}")
    Call<TransactionOptionsResponse> getTransactionOptions(@Path("snap_token") String snapToken);

}