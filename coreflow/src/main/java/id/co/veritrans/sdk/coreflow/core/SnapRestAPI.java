package id.co.veritrans.sdk.coreflow.core;

import id.co.veritrans.sdk.coreflow.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.models.snap.Transaction;
import id.co.veritrans.sdk.coreflow.models.snap.payment.BankTransferPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.BasePaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.CreditCardPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.IndosatDompetkuPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.KlikBCAPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.MandiriClickPayPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.TelkomselEcashPaymentRequest;
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

    /*
     * Get snap transaction details using Snap Endpoint.
     */
    @GET("/v1/payment_pages/{snap_token}")
    void getSnapTransaction(@Path("snap_token") String snapToken, Callback<Transaction> transactionCallback);

    /**
     * Charge payment using credit card token.
     *
     * @param creditCardPaymentRequest    Payment Request Details.
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_credit_card")
    void paymentUsingCreditCard(@Body CreditCardPaymentRequest creditCardPaymentRequest, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using bank transfer.
     *
     * @param bankTransferPaymentRequest  Payment Request Details.
     * @param transactionResponseCallback Callback
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_bank_transfer")
    void paymentUsingBankTransfer(@Body BankTransferPaymentRequest bankTransferPaymentRequest, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using KlikBCA.
     *
     * @param klikBCAPaymentRequest       Payment Request Details.
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_klikbca")
    void paymentUsingKlikBCA(@Body KlikBCAPaymentRequest klikBCAPaymentRequest, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using BCA Klikpay.
     *
     * @param basePaymentRequest Payment Request Details.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_bca_klikpay")
    void paymentUsingBCAKlikPay(@Body BasePaymentRequest basePaymentRequest, Callback<TransactionResponse> transactionResponseCallback);



    /**
     * Charge payment using permata bank.
     *
     * @param request bankTransferPaymentrqeust
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_permata")
    void paymentUsingBankTransferPermata(BankTransferPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);


    /**
     * Charge payment using bank tranfer bca.
     *
     * @param request BankTransferPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_bank_transfer_bca")
    void paymentUsingBankTransferBCA(BankTransferPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);


    /**
     * Charge payment using mandiri bill pay.
     *
     * @param request BankTransferPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_mandiri_billpayment")
    void paymentUsingMandiriBillPay(BankTransferPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);


    /**
     * Charge payment using mandiri click pay.
     *
     * @param request MandiriClickPayPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_mandiri_clickpay")
    void paymentUsingMandiriClickPay(MandiriClickPayPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using cimb clicks.
     *
     * @param request BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_cimb_clicks")
    void paymentUsingCIMBClick(BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using bri epay.
     *
     * @param request BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_bri_epay")
    void paymentUsingBRIEpay(BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using mandiri ecash.
     *
     * @param request BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_mandiri_ecash")
    void paymentUsingMandiriEcash(BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using telkomsel cash.
     *
     * @param request TelkomselEcashPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_telkomsel_cash")
    void paymentUsingTelkomselEcash(TelkomselEcashPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using telkomsel cash.
     *
     * @param request BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_xl_tunai")
    void paymentUsingXlTunai(BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using indosat dompetku.
     *
     * @param request IndosatDompetkuPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_indosat_dompetku")
    void paymentUsingIndomaret(BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);


    /**
     * Charge payment using indosat dompetku.
     *
     * @param request IndosatDompetkuPaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_indosat_dompetku")
    void paymentUsingIndosatDompetku(IndosatDompetkuPaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using kiosan.
     *
     * @param request BasePaymentRequest model
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_indosat_dompetku")
    void paymentUsingKiosan(BasePaymentRequest request, Callback<TransactionResponse> transactionResponseCallback);

}
