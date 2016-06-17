package id.co.veritrans.sdk.coreflow.core;

import id.co.veritrans.sdk.coreflow.models.AuthModel;
import id.co.veritrans.sdk.coreflow.models.BBMMoneyRequestModel;
import id.co.veritrans.sdk.coreflow.models.BCABankTransfer;
import id.co.veritrans.sdk.coreflow.models.BCAKlikPayModel;
import id.co.veritrans.sdk.coreflow.models.CIMBClickPayModel;
import id.co.veritrans.sdk.coreflow.models.CardRegistrationResponse;
import id.co.veritrans.sdk.coreflow.models.CardResponse;
import id.co.veritrans.sdk.coreflow.models.CardTransfer;
import id.co.veritrans.sdk.coreflow.models.DeleteCardResponse;
import id.co.veritrans.sdk.coreflow.models.EpayBriTransfer;
import id.co.veritrans.sdk.coreflow.models.GetOffersResponseModel;
import id.co.veritrans.sdk.coreflow.models.IndomaretRequestModel;
import id.co.veritrans.sdk.coreflow.models.IndosatDompetkuRequest;
import id.co.veritrans.sdk.coreflow.models.KlikBcaModel;
import id.co.veritrans.sdk.coreflow.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.coreflow.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.coreflow.models.MandiriECashModel;
import id.co.veritrans.sdk.coreflow.models.PermataBankTransfer;
import id.co.veritrans.sdk.coreflow.models.RegisterCardResponse;
import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;
import id.co.veritrans.sdk.coreflow.models.SaveCardResponse;
import id.co.veritrans.sdk.coreflow.models.TokenDetailsResponse;
import id.co.veritrans.sdk.coreflow.models.TransactionCancelResponse;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.models.TransactionStatusResponse;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface PaymentAPI {

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
     *
     * @return callback of transaction response
     */
    @GET("/token/")
    void getTokenTwoClick(
            @Query("card_cvv") String cardCVV,
            @Query("token_id") String tokenId,
            @Query("two_click") boolean twoClick,
            @Query("secure") boolean secure,
            @Query("gross_amount") double grossAmount,
            @Query("bank") String bank,
            @Query("client_key") String clientKey, Callback<TokenDetailsResponse> callback);

    @GET("/token/")
    void get3DSToken(@Query("card_number") String cardNumber,
                     @Query("card_cvv") String cardCVV,
                     @Query("card_exp_month") String cardExpiryMonth,
                     @Query("card_exp_year") String cardExpiryYear,
                     @Query("client_key") String clientKey,
                     @Query("bank") String bank,
                     @Query("secure") boolean secure,
                     @Query("two_click") boolean twoClick,
                     @Query("gross_amount") double grossAmount,
                     Callback<TokenDetailsResponse> callback
    );

    //http://api.sandbox.veritrans.co.id/v2/10938010/cancel/
    @Headers({"Content-Type: application/json",
            "Accept: application/json"})
    @POST("/{id}/cancel/")
    void cancelTransaction(
            @Header("x-auth") String auth,
            @Path("id") String transactionId,
            Callback<TransactionCancelResponse> callback);

    //http://api.sandbox.veritrans.co.id/v2/39b690a3-d626-4577-a6ab-14e29a1c74ac/status/
    @Headers({"Content-Type: application/json",
            "Accept: application/json"})
    @POST("/{id}/status/")
    void  transactionStatus(
            @Header("x-auth") String auth, @Path("id") String transactionId,
            Callback<TransactionStatusResponse> callback);

    //bank transfer
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingPermataBank(@Header("x-auth")
                                                            String authorization,
                                                            @Body PermataBankTransfer
                                                                    permataBankTransfer,
                                         Callback<TransactionResponse> callback);

    /**
     * Do the payment using BCA VA.
     *
     * @param authorization   authorization token.
     * @param bcaBankTransfer transaction details
     * @return callback of transaction response
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingBCAVA(
            @Header("x-auth") String authorization,
            @Body BCABankTransfer bcaBankTransfer,
            Callback<TransactionResponse> callback);

    //debit card
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingCard(
            @Header("x-auth") String auth, @Body CardTransfer
            cardTransfer, Callback<TransactionResponse> callback);

    //mandiri click pay
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingMandiriClickPay(
            @Header("x-auth") String auth
            , @Body MandiriClickPayRequestModel
                    mandiriClickPayRequestModel, Callback<TransactionResponse> callback);

    /**
     * Do payment using BCA Klik Pay.
     *
     * @param auth            Client authentication key.
     * @param bcaKlikPayModel Request body.
     * @return callback of the Transaction Response object.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingBCAKlikPay(
            @Header("x-auth") String auth,
            @Body BCAKlikPayModel bcaKlikPayModel,
            Callback<TransactionResponse> callback
    );

    /**
     * Do payment using Klik BCA.
     *
     * @param klikBcaModel Klik BCA description
     * @return Observable of the Transaction Response object.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingKlikBCA(@Body KlikBcaModel klikBcaModel,
                                     Callback<TransactionResponse> callback);

    //mandiri bill pay
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingMandiriBillPay(@Header("x-auth") String auth,
                                                               @Body MandiriBillPayTransferModel
                                                                       mandiriBillPayTransferModel,
                                            Callback<TransactionResponse> callback);

    //epay bri transaction flow
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingEpayBri(@Header("x-auth") String auth,
                                     @Body EpayBriTransfer
                                             epayBriTransfer,
                                     Callback<TransactionResponse> callback);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingIndosatDompetku(@Header("x-auth") String auth,
                                     @Body IndosatDompetkuRequest
                                             indosatDompetkuRequest,
                                     Callback<TransactionResponse> callback);

    //CIMB transaction flow
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingCIMBClickPay(@Header("x-auth") String auth,
                                  @Body CIMBClickPayModel
                                          cimbClickPayModel,
                                  Callback<TransactionResponse> callback);

    //Mandiri E Cash transaction flow
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingMandiriECash(@Header("x-auth") String auth,
                                  @Body MandiriECashModel
                                          mandiriECashModel,
                                  Callback<TransactionResponse> callback);

    //indomaret payment
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingIndomaret(@Header("x-auth") String auth,
                               @Body IndomaretRequestModel
                                       indomaretRequestModel,
                               Callback<TransactionResponse> callback);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/creditcard")
    void registerCard(@Header("x-auth") String auth,
                      @Body RegisterCardResponse registerCardResponse,
                      Callback<CardResponse> callback);


    //save cards or get cards
    @Deprecated
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/card/register")
    void saveCard(@Header("x-auth") String auth,
                  @Body SaveCardRequest cardTokenRequest,
                  Callback<SaveCardResponse> callback);

    //save cards or get cards
    @Deprecated
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/card/")
    void getCard(@Header("x-auth") String auth, Callback<CardResponse> callback);

    //delete card
    @Deprecated
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @DELETE("/card/{saved_token_id}")
    void deleteCard(@Header("x-auth") String auth, @Path("saved_token_id") String savedTokenId,
                    Callback<DeleteCardResponse> callback);

    //BBMMoney Payment
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingBBMMoney(@Header("x-auth") String auth,
                              @Body BBMMoneyRequestModel
                                      bbmMoneyRequestModel,
                              Callback<TransactionResponse> callback);

    //get offers
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/promotions")
    void getOffers(@Header("x-auth") String auth, Callback<GetOffersResponseModel> callback);

    // register credit card info
    @GET("/card/register")
    void registerCard(@Query("card_number") String cardNumber,
                      @Query("card_exp_month") String cardExpMonth,
                      @Query("card_exp_year") String cardExpYear,
                      @Query("client_key") String clientKey,
                      Callback<RegisterCardResponse> callback);


    /**
     * For instalment offers get token
     *
     * @param cardCVV        card cvv number
     * @param tokenId        token identifier
     * @param twoClick       is two click or not
     * @param secure         is secure or not
     * @param grossAmount    gross amount
     * @param bank           bank name
     * @param clientKey      client key
     * @param instalment     installment
     * @param instalmentTerm installment terms
     * @return callback of transaction response
     */

    @GET("/token/")
    void getTokenInstalmentOfferTwoClick(
            @Query("card_cvv") String cardCVV,
            @Query("token_id") String tokenId,
            @Query("two_click") boolean twoClick,
            @Query("secure") boolean secure,
            @Query("gross_amount") double grossAmount,
            @Query("bank") String bank,
            @Query("client_key") String clientKey,
            @Query("installment") boolean instalment,
            @Query("installment_term") String instalmentTerm,
            Callback<TokenDetailsResponse> callback
    );

    /***
     * Get instalment offers 3ds token
     *
     * @param cardNumber      card number
     * @param cardCVV         card cvv number
     * @param cardExpiryMonth card expiry's month
     * @param cardExpiryYear  card expiry's year
     * @param clientKey       client key
     * @param bank            bank name
     * @param secure          is secure
     * @param twoClick        is two click
     * @param grossAmount     gross amount
     * @return callback of transaction response
     */

    @GET("/token/")
    void get3DSTokenInstalmentOffers(@Query("card_number") String cardNumber,
                                     @Query("card_cvv") String cardCVV,
                                     @Query("card_exp_month") String cardExpiryMonth,
                                     @Query("card_exp_year") String cardExpiryYear,
                                     @Query("client_key") String clientKey,
                                     @Query("bank") String bank,
                                     @Query("secure") boolean secure,
                                     @Query("two_click") boolean twoClick,
                                     @Query("gross_amount") double grossAmount,
                                     @Query("installment") boolean instalment,
                                     @Query("installment_term") String
                                             instalmentTerm,
                                     Callback<TokenDetailsResponse> callback
    );

    /**
     * Register card into Veritrans API.
     *
     * @param cardNumber      credit card number
     * @param cardCVV         credit card cvv number
     * @param cardExpiryMonth credit card expiry month in number
     * @param cardExpiryYear  credit card expiry year in 4 digit (example: 2020)
     * @param clientKey       veritrans API client key
     * @return callback of token
     */
    @Headers({"Content-Type: application/json", "x-auth: da53847171259b511488cf366e701050"})
    @GET("/card/register")
    void registerCard(
            @Query("card_number") String cardNumber,
            @Query("card_cvv") String cardCVV,
            @Query("card_exp_month") String cardExpiryMonth,
            @Query("card_exp_year") String cardExpiryYear,
            @Query("client_key") String clientKey, Callback<CardRegistrationResponse> callback
    );

    /**
     * Get authentication token from merchant server
     *
     * @return authentication token.
     */
    @POST("/auth")
    void getAuthenticationToken(Callback<AuthModel> callback);
}