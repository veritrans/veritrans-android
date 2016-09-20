package com.midtrans.sdk.coreflow.core;

import com.midtrans.sdk.coreflow.models.TransactionResponse;
import com.midtrans.sdk.coreflow.models.snap.Transaction;
import com.midtrans.sdk.coreflow.models.snap.payment.BasePaymentRequest;
import com.midtrans.sdk.coreflow.models.snap.payment.CreditCardPaymentRequest;
import com.midtrans.sdk.coreflow.models.snap.payment.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.coreflow.models.snap.payment.KlikBCAPaymentRequest;
import com.midtrans.sdk.coreflow.models.snap.payment.MandiriClickPayPaymentRequest;
import com.midtrans.sdk.coreflow.models.snap.payment.TelkomselEcashPaymentRequest;
import com.midtrans.sdk.coreflow.models.snap.payment.BankTransferPaymentRequest;

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
     * @param snapToken snap token
     * @param transactionCallback response get transaction request
     */
    @GET("/v1/payment_pages/{snap_token}")
    void getPaymentOption(@Path("snap_token") String snapToken, Callback<Transaction> transactionCallback);

    /**
     * Charge payment using credit card token.
     *
     * @param creditCardPaymentRequest    Payment Request Details.
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "X-Source: mobile"})
    @POST("/v1/pay")
    void paymentUsingCreditCard(@Body CreditCardPaymentRequest creditCardPaymentRequest, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using bank transfer.
     *
     * @param bankTransferPaymentRequest  Payment Request Details.
     * @param transactionResponseCallback Callback
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "X-Source: mobile"})
    @POST("/v1/pay")
    void paymentUsingBankTransfer(@Body BankTransferPaymentRequest bankTransferPaymentRequest, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using KlikBCA.
     *
     * @param klikBCAPaymentRequest       Payment Request Details.
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "X-Source: mobile"})
    @POST("/v1/pay")
    void paymentUsingKlikBCA(@Body KlikBCAPaymentRequest klikBCAPaymentRequest, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using BCA Klikpay.
     *
     * @param basePaymentRequest Payment Request Details.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "X-Source: mobile"})
    @POST("/v1/pay")
    void paymentUsingBCAKlikPay(@Body BasePaymentRequest basePaymentRequest, Callback<TransactionResponse> transactionResponseCallback);


    /**
     * Charge payment using permata bank.
     *
     * @param request                     bankTransferPaymentrqeust
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "X-Source: mobile"})
    @POST("/v1/pay")
    void paymentUsingBankTransferPermata(@Body BankTransferPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);


    /**
     * Charge payment using bank tranfer bca.
     *
     * @param request                     BankTransferPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "X-Source: mobile"})
    @POST("/v1/pay")
    void paymentUsingBankTransferBCA(@Body BankTransferPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);


    /**
     * Charge payment using mandiri bill pay.
     *
     * @param request                     BankTransferPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "X-Source: mobile"})
    @POST("/v1/pay")
    void paymentUsingMandiriBillPay(@Body BankTransferPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);


    /**
     * Charge payment using mandiri click pay.
     *
     * @param request                     MandiriClickPayPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "X-Source: mobile"})
    @POST("/v1/pay")
    void paymentUsingMandiriClickPay(@Body MandiriClickPayPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using cimb clicks.
     *
     * @param request                     BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "X-Source: mobile"})
    @POST("/v1/pay")
    void paymentUsingCIMBClick(@Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using bri epay.
     *
     * @param request                     BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "X-Source: mobile"})
    @POST("/v1/pay")
    void paymentUsingBRIEpay(@Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using mandiri ecash.
     *
     * @param request                     BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "X-Source: mobile"})
    @POST("/v1/pay")
    void paymentUsingMandiriEcash(@Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using telkomsel cash.
     *
     * @param request                     TelkomselEcashPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "X-Source: mobile"})
    @POST("/v1/pay")
    void paymentUsingTelkomselEcash(@Body TelkomselEcashPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using telkomsel cash.
     *
     * @param request                     BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "X-Source: mobile"})
    @POST("/v1/pay")
    void paymentUsingXlTunai(@Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using indosat dompetku.
     *
     * @param request                     IndosatDompetkuPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "X-Source: mobile"})
    @POST("/v1/pay")
    void paymentUsingIndomaret(@Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);


    /**
     * Charge payment using indosat dompetku.
     *
     * @param request                     IndosatDompetkuPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "X-Source: mobile"})
    @POST("/v1/pay")
    void paymentUsingIndosatDompetku(@Body IndosatDompetkuPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using kiosan.
     *
     * @param request                     BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "X-Source: mobile"})
    @POST("/v1/pay")
    void paymentUsingKiosan(@Body BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using Bank Transfer All Bank.
     *
     * @param request                     BasePaymentRequest model.
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "X-Source: mobile"})
    @POST("/v1/pay")
    void paymentUsingBankTransferAllBank(@Body BankTransferPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);
}
