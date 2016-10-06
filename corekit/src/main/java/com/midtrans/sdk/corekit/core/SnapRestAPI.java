package com.midtrans.sdk.corekit.core;

import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.payment.BankTransferPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.BasePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.CreditCardPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.KlikBCAPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.MandiriClickPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.TelkomselEcashPaymentRequest;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
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
    @GET("/v1/payment_pages/{snap_token}")
    void getPaymentOption(@Path("snap_token") String snapToken, Callback<Transaction> transactionCallback);

    /**
     * Charge payment using credit card token.
     *
     * @param authenticationToken
     * @param creditCardPaymentRequest    Payment Request Details.
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingCreditCard(@Path("token") String authenticationToken, @Body CreditCardPaymentRequest creditCardPaymentRequest, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using bank transfer Virtual account.
     *
     * @param paymentRequest  Payment Request Details.
     * @param transactionResponseCallback Callback
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingBankTransfer(@Path("token") String authenticationToken, @Body BankTransferPaymentRequest paymentRequest, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using KlikBCA.
     *
     * @param authenticationToken
     * @param klikBCAPaymentRequest       Payment Request Details.
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingKlikBCA(@Path("token") String authenticationToken, @Body KlikBCAPaymentRequest klikBCAPaymentRequest, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using BCA Klikpay.
     *
     * @param authenticationToken
     * @param basePaymentRequest Payment Request Details.
     * @param transactionResponseCallback transaction callback
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingBCAKlikPay(@Path("token") String authenticationToken, @Body BasePaymentRequest basePaymentRequest, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using mandiri bill pay.
     *
     * @param authenticationToken
     * @param request                     BankTransferPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingMandiriBillPay(@Path("token") String authenticationToken, @Body BankTransferPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);


    /**
     * Charge payment using mandiri click pay.
     *
     * @param authenticationToken
     * @param request                     MandiriClickPayPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingMandiriClickPay(@Path("token") String authenticationToken, @Body MandiriClickPayPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using cimb clicks.
     *
     * @param authenticationToken
     * @param request                     BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingCIMBClick(@Path("token") String authenticationToken, @Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using bri epay.
     *
     * @param authenticationToken
     * @param request                     BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingBRIEpay(@Path("token") String authenticationToken, @Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using mandiri ecash.
     *
     * @param authenticationToken
     * @param request                     BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingMandiriEcash(@Path("token") String authenticationToken, @Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using telkomsel cash.
     *
     * @param authenticationToken
     * @param request                     TelkomselEcashPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingTelkomselEcash(@Path("token") String authenticationToken, @Body TelkomselEcashPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using telkomsel cash.
     *
     * @param authenticationToken
     * @param request                     BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingXlTunai(@Path("token") String authenticationToken, @Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using indosat dompetku.
     *
     * @param authenticationToken
     * @param request                     IndosatDompetkuPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingIndomaret(@Path("token") String authenticationToken, @Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);


    /**
     * Charge payment using indosat dompetku.
     *@param authenticationToken
     * @param request                     IndosatDompetkuPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingIndosatDompetku(@Path("token") String authenticationToken, @Body IndosatDompetkuPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using kiosan.
     *
     * @param authenticationToken
     * @param request                     BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/transactions/{token}/pay")
    void paymentUsingKiosan(@Path("token") String authenticationToken, @Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

}
