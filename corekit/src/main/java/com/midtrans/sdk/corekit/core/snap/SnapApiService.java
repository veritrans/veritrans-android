package com.midtrans.sdk.corekit.core.snap;

import com.midtrans.sdk.corekit.core.snap.model.pay.request.va.BankTransferPaymentRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.BcaPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.BniPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.OtherPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.PermataPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.transaction.response.PaymentInfoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SnapApiService {

    /**
     * Get transaction options using Snap with snap token.
     *
     * @param snapToken snap token
     */
    @GET("v1/transactions/{snap_token}")
    Call<PaymentInfoResponse> getTransactionOptions(@Path("snap_token") String snapToken);

    /**
     * Charge payment using bank transfer Virtual account.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST("v1/transactions/{snap_token}/pay")
    Call<BcaPaymentResponse> paymentBankTransferBca(@Path("snap_token") String snapToken, @Body BankTransferPaymentRequest paymentRequest);

    /**
     * Charge payment using bank transfer Virtual account.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST("v1/transactions/{snap_token}/pay")
    Call<BniPaymentResponse> paymentBankTransferBni(@Path("snap_token") String snapToken, @Body BankTransferPaymentRequest paymentRequest);


    /**
     * Charge payment using bank transfer Virtual account.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST("v1/transactions/{snap_token}/pay")
    Call<PermataPaymentResponse> paymentBankTransferPermata(@Path("snap_token") String snapToken, @Body BankTransferPaymentRequest paymentRequest);


    /**
     * Charge payment using bank transfer Virtual account.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST("v1/transactions/{snap_token}/pay")
    Call<OtherPaymentResponse> paymentBankTransferOther(@Path("snap_token") String snapToken, @Body BankTransferPaymentRequest paymentRequest);

}