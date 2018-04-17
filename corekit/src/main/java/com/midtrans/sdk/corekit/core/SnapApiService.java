package com.midtrans.sdk.corekit.core;

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
import com.midtrans.sdk.corekit.models.snap.payment.GoPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.KlikBCAPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.NewMandiriClickPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.TelkomselEcashPaymentRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Created by ziahaqi on 3/26/18.
 */

public interface SnapApiService {

    /**
     * Get snap transaction details using Snap Endpoint.
     *
     * @param snapToken snap token
     */
    @GET("v1/transactions/{snap_token}")
    Call<Transaction> getPaymentOption(@Path("snap_token") String snapToken);

    /**
     * Charge payment using credit card token.
     *
     * @param snapToken
     * @param creditCardPaymentRequest Payment Request Details.
     */
    @POST("v1/transactions/{snap_token}/pay")
    Call<TransactionResponse> paymentUsingCreditCard(@Path("snap_token") String snapToken, @Body CreditCardPaymentRequest creditCardPaymentRequest);

    /**
     * Charge payment using bank transfer Virtual account.
     *
     * @param paymentRequest Payment Request Details.
     */
    @POST("v1/transactions/{snap_token}/pay")
    Call<TransactionResponse> paymentUsingVa(@Path("snap_token") String snapToken, @Body BankTransferPaymentRequest paymentRequest);

    /**
     * Charge payment using BCA Klikpay.
     *
     * @param snapToken
     * @param basePaymentRequest Payment Request Details.
     */
    @POST("v1/transactions/{snap_token}/pay")
    Call<TransactionResponse> paymentUsingBaseMethod(@Path("snap_token") String snapToken, @Body BasePaymentRequest basePaymentRequest);


    /**
     * Charge payment using KlikBCA.
     *
     * @param snapToken
     * @param klikBCAPaymentRequest Payment Request Details.
     */
    @POST("v1/transactions/{snap_token}/pay")
    Call<TransactionResponse> paymentUsingKlikBca(@Path("snap_token") String snapToken, @Body KlikBCAPaymentRequest klikBCAPaymentRequest);

    /**
     * Charge payment using new flow mandiri click pay.
     *
     * @param snapToken
     * @param request   MandiriClickPayPaymentRequest model
     */
    @POST("v1/transactions/{snap_token}/pay")
    Call<TransactionResponse> paymentUsingMandiriClickPay(@Path("snap_token") String snapToken, @Body NewMandiriClickPayPaymentRequest request);

    /**
     * Charge payment using telkomsel cash.
     *
     * @param snapToken
     * @param request   TelkomselEcashPaymentRequest model
     */
    @POST("v1/transactions/{snap_token}/pay")
    Call<TransactionResponse> paymentUsingTelkomselEcash(@Path("snap_token") String snapToken, @Body TelkomselEcashPaymentRequest request);

    /**
     * Charge payment using indosat dompetku.
     *
     * @param snapToken
     * @param request   IndosatDompetkuPaymentRequest model
     */
    @POST("v1/transactions/{snap_token}/pay")
    Call<TransactionResponse> paymentUsingIndosatDompetku(@Path("snap_token") String snapToken, @Body IndosatDompetkuPaymentRequest request);

    /**
     * Charge payment using GCI (Gift Card Indonesia)
     *
     * @param snapToken
     * @param paymentRequest
     */
    @POST("v1/transactions/{snap_token}/pay")
    Call<TransactionResponse> paymentUsingGci(@Path("snap_token") String snapToken, @Body GCIPaymentRequest paymentRequest);

    /**
     * Charge payment using GoPay
     *
     * @param snapToken
     * @param paymentRequest
     */
    @POST("v1/transactions/{snap_token}/pay")
    Call<TransactionResponse> paymentUsingGoPay(@Path("snap_token") String snapToken, @Body GoPayPaymentRequest paymentRequest);

    /**
     * Charge payment using Danamon Online
     *
     * @param snapToken      SnapToken
     * @param paymentRequest DanamonOnlinePaymentRequest
     */
    @POST("v1/transactions/{snap_token}/pay")
    Call<TransactionResponse> paymentUsingDanamonOnline(@Path("snap_token") String snapToken, @Body DanamonOnlinePaymentRequest paymentRequest);

    /**
     * Get bins of credit card
     */
    @GET("v1/bank_bins")
    Call<List<BankBinsResponse>> getBankBins();

    @DELETE("v1/transactions/{snap_token}/saved_tokens/{masked_card}")
    Call<Void> deleteCard(@Path("snap_token") String snapToken, @Path("masked_card") String maskedCard);


    /**
     * Get Banks Points from snap backend.
     *
     * @param snapToken snap token
     * @param cardToken credit card token
     */
    @GET("v1/transactions/{snap_token}/point_inquiry/{card_token}")
    Call<BanksPointResponse> getBanksPoint(@Path("snap_token") String snapToken, @Path("card_token") String cardToken);


    /**
     * Get Transaction Status.
     *
     * @param snapToken snap token
     */
    @GET("v1/transactions/{snap_token}/status")
    Call<TransactionStatusResponse> getTransactionStatus(@Path("snap_token") String snapToken);

}
