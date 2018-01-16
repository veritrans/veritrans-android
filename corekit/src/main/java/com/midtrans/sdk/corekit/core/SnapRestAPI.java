package com.midtrans.sdk.corekit.core;

import com.midtrans.sdk.corekit.models.GoPayResendAuthorizationResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.BanksPointResponse;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.TransactionStatusResponse;
import com.midtrans.sdk.corekit.models.snap.payment.BankTransferPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.BasePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.CreditCardPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.DanamonOnlinePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GCIPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GoPayAuthorizationRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GoPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.KlikBCAPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.MandiriClickPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.NewMandiriClickPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.TelkomselEcashPaymentRequest;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * @author rakawm
 */
public interface SnapRestAPI {

    /**
     * Get snap transaction details using Snap Endpoint.
     *
     * @param snapToken           snap token
     * @param transactionCallback response get transaction request
     */
    @GET("/v1/transactions/{snap_token}")
    void getPaymentOption(@Path("snap_token") String snapToken, Callback<Transaction> transactionCallback);

    /**
     * Charge payment using credit card token.
     *
     * @param authenticationToken
     * @param creditCardPaymentRequest    Payment Request Details.
     * @param transactionResponseCallback Callback.
     */
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingCreditCard(@Path("token") String authenticationToken, @Body CreditCardPaymentRequest creditCardPaymentRequest, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using bank transfer Virtual account.
     *
     * @param paymentRequest              Payment Request Details.
     * @param transactionResponseCallback Callback
     */
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingBankTransfer(@Path("token") String authenticationToken, @Body BankTransferPaymentRequest paymentRequest, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using KlikBCA.
     *
     * @param authenticationToken
     * @param klikBCAPaymentRequest       Payment Request Details.
     * @param transactionResponseCallback Callback.
     */
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingKlikBCA(@Path("token") String authenticationToken, @Body KlikBCAPaymentRequest klikBCAPaymentRequest, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using BCA Klikpay.
     *
     * @param authenticationToken
     * @param basePaymentRequest          Payment Request Details.
     * @param transactionResponseCallback transaction callback
     */
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingBCAKlikPay(@Path("token") String authenticationToken, @Body BasePaymentRequest basePaymentRequest, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using mandiri bill pay.
     *
     * @param authenticationToken
     * @param request                     BankTransferPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingMandiriBillPay(@Path("token") String authenticationToken, @Body BankTransferPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);


    /**
     * Charge payment using mandiri click pay.
     *
     * Deprecated, please see {@link com.midtrans.sdk.corekit.core.SnapRestAPI#paymentUsingMandiriClickPay(String, NewMandiriClickPayPaymentRequest, Callback)}
     *
     * @param authenticationToken
     * @param request                     MandiriClickPayPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Deprecated
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingMandiriClickPay(@Path("token") String authenticationToken, @Body MandiriClickPayPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using new flow mandiri click pay.
     *
     * @param authenticationToken
     * @param request                     MandiriClickPayPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingMandiriClickPay(@Path("token") String authenticationToken, @Body NewMandiriClickPayPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);


    /**
     * Charge payment using cimb clicks.
     *
     * @param authenticationToken
     * @param request                     BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingCIMBClick(@Path("token") String authenticationToken, @Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using bri epay.
     *
     * @param authenticationToken
     * @param request                     BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingBRIEpay(@Path("token") String authenticationToken, @Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using mandiri ecash.
     *
     * @param authenticationToken
     * @param request                     BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingMandiriEcash(@Path("token") String authenticationToken, @Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using telkomsel cash.
     *
     * @param authenticationToken
     * @param request                     TelkomselEcashPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingTelkomselEcash(@Path("token") String authenticationToken, @Body TelkomselEcashPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using telkomsel cash.
     *
     * @param authenticationToken
     * @param request                     BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingXlTunai(@Path("token") String authenticationToken, @Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using indosat dompetku.
     *
     * @param authenticationToken
     * @param request                     IndosatDompetkuPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingIndomaret(@Path("token") String authenticationToken, @Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);


    /**
     * Charge payment using indosat dompetku.
     *
     * @param authenticationToken
     * @param request                     IndosatDompetkuPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingIndosatDompetku(@Path("token") String authenticationToken, @Body IndosatDompetkuPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using kiosan.
     *
     * @param authenticationToken
     * @param request                     BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingKiosan(@Path("token") String authenticationToken, @Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using GCI (Gift Card Indonesia)
     *
     * @param authenticationToken
     * @param paymentRequest
     * @param callback
     */
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingGCI(@Path("token") String authenticationToken, @Body GCIPaymentRequest paymentRequest, Callback<TransactionResponse> callback);

    /**
     * Charge payment using GoPay
     *
     * @param snapToken
     * @param paymentRequest
     * @param callback
     */
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingGoPay(@Path("token") String snapToken, @Body GoPayPaymentRequest paymentRequest, Callback<TransactionResponse> callback);

    /**
     * Authorize GO-PAY payment
     *
     * @param snapToken
     * @param request
     * @param callback
     */
    @POST("/v1/gopay/{token}/pay")
    void authorizeGoPayPayment(@Path("token") String snapToken, @Body GoPayAuthorizationRequest request, Callback<TransactionResponse> callback);


    /**
     * Resend GO-PAY authorization
     *
     * @param snapToken
     * @param callback
     */
    @POST("/v1/gopay/{token}/resend")
    void resendGoPayAuthorization(@Path("token") String snapToken, Callback<GoPayResendAuthorizationResponse> callback);

    /**
     * Charge payment using Danamon Online
     *
     * @param snapToken SnapToken
     * @param paymentRequest DanamonOnlinePaymentRequest
     * @param callback TransactionResponseCallback
     */
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingDanamonOnline(@Path("token") String snapToken, @Body DanamonOnlinePaymentRequest paymentRequest, Callback<TransactionResponse> callback);

    /**

    /**
     * @param callback callback
     */
    @GET("/v1/bank_bins")
    void getBankBins(Callback<ArrayList<BankBinsResponse>> callback);

    @DELETE("/v1/transactions/{token}/saved_tokens/{masked_card}")
    void deleteCard(@Path("token") String authenticationToken, @Path("masked_card") String maskedCard, Callback<Void> callback);


    /**
     * Get Banks Points from snap backend.
     *
     * @param snapToken           snap token
     * @param transactionCallback response get transaction request
     */
    @GET("/v1/transactions/{snap_token}/point_inquiry/{card_token}")
    void getBanksPoint(@Path("snap_token") String snapToken, @Path("card_token") String cardToken, Callback<BanksPointResponse> transactionCallback);


    /**
     * Get Transaction Status.
     *
     * @param snapToken snap token
     * @param callback  response get transaction request
     */
    @GET("/v1/transactions/{snap_token}/status")
    void getTransactionStatus(@Path("snap_token") String snapToken, Callback<TransactionStatusResponse> callback);

}
