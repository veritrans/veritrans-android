package id.co.veritrans.sdk.core;

import id.co.veritrans.sdk.models.BBMMoneyRequestModel;
import id.co.veritrans.sdk.models.CIMBClickPayModel;
import id.co.veritrans.sdk.models.CardTransfer;
import id.co.veritrans.sdk.models.EpayBriTransfer;
import id.co.veritrans.sdk.models.IndomaretRequestModel;
import id.co.veritrans.sdk.models.IndosatDompetkuRequest;
import id.co.veritrans.sdk.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.models.MandiriECashModel;
import id.co.veritrans.sdk.models.PermataBankTransfer;
import id.co.veritrans.sdk.models.TokenDetailsResponse;
import id.co.veritrans.sdk.models.TransactionCancelResponse;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.models.TransactionStatusResponse;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by shivam on 10/26/15.
 */
public interface VeritranceApiInterface {

    //token?card_number=4811111111111114&card_cvv=123&card_exp_month=06&card_exp_year=2020
    // &client_key=VT-client-Lre_JFh5klhfGefF
   /* @GET("/token/")
    Observable<TokenDetailsResponse> getToken(@Query("card_number") String cardNumber,
                                              @Query("card_cvv") int cardCVV,
                                              @Query("card_exp_month") int cardExpiryMonth,
                                              @Query("card_exp_year") int cardExpiryYear,
                                              @Query("client_key") String clientKey,
                                              @Query("gross_amount") double grossAmount,
                                              @Query("bank") String bank
    );*/

    /**
     * card_cvv, token_id, two_click, bank, secure, gross_amount
     * this api call hit veritrans server
     */
    @GET("/token/")
    Observable<TokenDetailsResponse> getTokenTwoClick(
            @Query("card_cvv") int cardCVV,
            @Query("token_id") String tokenId,
            @Query("two_click") boolean twoClick,
            @Query("secure") boolean secure,
            @Query("gross_amount") double grossAmount,
            @Query("bank") String bank,
            @Query("client_key") String clientKey);

    @GET("/token/")
    Observable<TokenDetailsResponse> get3DSToken(@Query("card_number") String cardNumber,
                                                 @Query("card_cvv") int cardCVV,
                                                 @Query("card_exp_month") int cardExpiryMonth,
                                                 @Query("card_exp_year") int cardExpiryYear,
                                                 @Query("client_key") String clientKey,
                                                 @Query("bank") String bank,
                                                 @Query("secure") boolean secure,
                                                 @Query("two_click") boolean twoClick,
                                                 @Query("gross_amount") double grossAmount
    );

    //http://api.sandbox.veritrans.co.id/v2/10938010/cancel/
    @Headers({"Content-Type: application/json",
            "Accept: application/json"})
    @POST("/{id}/cancel/")
    Observable<TransactionCancelResponse> cancelTransaction(
            @Header("x-auth") String auth,
            @Path("id") String transactionId);

    //http://api.sandbox.veritrans.co.id/v2/39b690a3-d626-4577-a6ab-14e29a1c74ac/status/
    @Headers({"Content-Type: application/json",
            "Accept: application/json"})
    @POST("/{id}/status/")
    Observable<TransactionStatusResponse> transactionStatus(
            @Header("x-auth") String auth, @Path("id") String transactionId);

    //bank transfer
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    Observable<TransactionResponse> paymentUsingPermataBank(@Header("x-auth")
                                                            String authorization,
                                                            @Body PermataBankTransfer
                                                                    permataBankTransfer);
    //debit card
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    Observable<TransactionResponse> paymentUsingCard(
            @Header("x-auth") String auth, @Body CardTransfer
            cardTransfer);

    //mandiri click pay
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    Observable<TransactionResponse> paymentUsingMandiriClickPay(
                                                                @Header("x-auth") String auth
            , @Body MandiriClickPayRequestModel
                                                                        mandiriClickPayRequestModel);

    //mandiri bill pay
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    Observable<TransactionResponse> paymentUsingMandiriBillPay(@Header("x-auth") String auth,
                                                               @Body MandiriBillPayTransferModel
                                                                       mandiriBillPayTransferModel);

    //epay bri transaction flow
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    Observable<TransactionResponse> paymentUsingEpayBri(@Header("x-auth") String auth,
                                                        @Body EpayBriTransfer
                                                                epayBriTransfer);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    Observable<TransactionResponse> paymentUsingIndosatDompetku(@Header("x-auth") String auth,
                                                                @Body IndosatDompetkuRequest
                                                                        indosatDompetkuRequest);

    //CIMB transaction flow
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    Observable<TransactionResponse> paymentUsingCIMBClickPay(@Header("x-auth") String auth,
                                                             @Body CIMBClickPayModel
                                                                     cimbClickPayModel);

    //Mandiri E Cash transaction flow
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    Observable<TransactionResponse> paymentUsingMandiriECash(@Header("x-auth") String auth,
                                                             @Body MandiriECashModel
                                                                     mandiriECashModel);

    //indomaret payment
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    Observable<TransactionResponse> paymentUsingIndomaret(@Header("x-auth") String auth,
                                                          @Body IndomaretRequestModel
                                                                  indomaretRequestModel);

    //BBMMoney Payment
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    Observable<TransactionResponse> paymentUsingBBMMoney(@Header("x-auth") String auth,
                                                          @Body BBMMoneyRequestModel
                                                                  bbmMoneyRequestModel);
}

