package id.co.veritrans.sdk.core;

import id.co.veritrans.sdk.models.AuthModel;
import id.co.veritrans.sdk.models.BBMMoneyRequestModel;
import id.co.veritrans.sdk.models.BCABankTransfer;
import id.co.veritrans.sdk.models.BCAKlikPayModel;
import id.co.veritrans.sdk.models.CIMBClickPayModel;
import id.co.veritrans.sdk.models.CardRegistrationResponse;
import id.co.veritrans.sdk.models.CardResponse;
import id.co.veritrans.sdk.models.CardTransfer;
import id.co.veritrans.sdk.models.DeleteCardResponse;
import id.co.veritrans.sdk.models.EpayBriTransfer;
import id.co.veritrans.sdk.models.GetOffersResponseModel;
import id.co.veritrans.sdk.models.IndomaretRequestModel;
import id.co.veritrans.sdk.models.IndosatDompetkuRequest;
import id.co.veritrans.sdk.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.models.MandiriECashModel;
import id.co.veritrans.sdk.models.PermataBankTransfer;
import id.co.veritrans.sdk.models.RegisterCardResponse;
import id.co.veritrans.sdk.models.SaveCardRequest;
import id.co.veritrans.sdk.models.SaveCardResponse;
import id.co.veritrans.sdk.models.TokenDetailsResponse;
import id.co.veritrans.sdk.models.TransactionCancelResponse;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.models.TransactionStatusResponse;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

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
     * @return observable of transaction response
     */
    @GET("/token/")
    Observable<TokenDetailsResponse> getTokenTwoClick(
            @Query("card_cvv") String cardCVV,
            @Query("token_id") String tokenId,
            @Query("two_click") boolean twoClick,
            @Query("secure") boolean secure,
            @Query("gross_amount") double grossAmount,
            @Query("bank") String bank,
            @Query("client_key") String clientKey);

    @GET("/token/")
    Observable<TokenDetailsResponse> get3DSToken(@Query("card_number") String cardNumber,
                                                 @Query("card_cvv") String cardCVV,
                                                 @Query("card_exp_month") String cardExpiryMonth,
                                                 @Query("card_exp_year") String cardExpiryYear,
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

    /**
     * Do the payment using BCA VA.
     * @param authorization     authorization token.
     * @param bcaBankTransfer   transaction details
     * @return observable of transaction response
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    Observable<TransactionResponse> paymentUsingBCAVA(
            @Header("x-auth") String authorization,
            @Body BCABankTransfer bcaBankTransfer);

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

    /**
     * Do payment using BCA Klik Pay.
     *
     * @param auth              Client authentication key.
     * @param bcaKlikPayModel   Request body.
     * @return                  Observable of the Transaction Response object.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    Observable<TransactionResponse> paymentUsingBCAKlikPay(
           @Header("x-auth") String auth,
           @Body BCAKlikPayModel bcaKlikPayModel
    );

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

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/creditcard")
    Observable<CardResponse> registerCard(@Header("x-auth") String auth,
                                          @Body RegisterCardResponse registerCardResponse);


    //save cards or get cards
    @Deprecated
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/card/register")
    Observable<SaveCardResponse> saveCard(@Header("x-auth") String auth,
                                      @Body SaveCardRequest cardTokenRequest);

    //save cards or get cards
    @Deprecated
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/card/")
    Observable<CardResponse> getCard(@Header("x-auth") String auth);

    //delete card
    @Deprecated
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @DELETE("/card/{saved_token_id}")
    Observable<DeleteCardResponse> deleteCard(@Header("x-auth") String auth, @Path("saved_token_id") String savedTokenId);

    //BBMMoney Payment
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    Observable<TransactionResponse> paymentUsingBBMMoney(@Header("x-auth") String auth,
                                                         @Body BBMMoneyRequestModel
                                                                 bbmMoneyRequestModel);

    //get offers
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/offers")
    Observable<GetOffersResponseModel> getOffers(@Header("x-auth") String auth);

    // register credit card info
    @GET("/card/register")
    Observable<RegisterCardResponse> registerCard(@Query("card_number") String cardNumber,
                                                 @Query("card_exp_month") String cardExpMonth,
                                                 @Query("card_exp_year") String cardExpYear,
                                                 @Query("client_key") String clientKey);


    /**
     * For instalment offers get token
     *
     * @param cardCVV       card cvv number
     * @param tokenId       token identifier
     * @param twoClick      is two click or not
     * @param secure        is secure or not
     * @param grossAmount   gross amount
     * @param bank          bank name
     * @param clientKey     client key
     * @param instalment    installment
     * @param instalmentTerm    installment terms
     * @return observable of transaction response
     */

    @GET("/token/")
    Observable<TokenDetailsResponse> getTokenInstalmentOfferTwoClick(
            @Query("card_cvv") String cardCVV,
            @Query("token_id") String tokenId,
            @Query("two_click") boolean twoClick,
            @Query("secure") boolean secure,
            @Query("gross_amount") double grossAmount,
            @Query("bank") String bank,
            @Query("client_key") String clientKey,
            @Query("installment") boolean instalment,
            @Query("installment_term") String instalmentTerm
    );

    /***
     * Get instalment offers 3ds token
     *
     * @param cardNumber        card number
     * @param cardCVV           card cvv number
     * @param cardExpiryMonth   card expiry's month
     * @param cardExpiryYear    card expiry's year
     * @param clientKey         client key
     * @param bank              bank name
     * @param secure            is secure
     * @param twoClick          is two click
     * @param grossAmount       gross amount
     * @return observable of transaction response
     */

    @GET("/token/")
    Observable<TokenDetailsResponse> get3DSTokenInstalmentOffers(@Query("card_number") String cardNumber,
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
                                                                         instalmentTerm
    );

    /**
     * Register card into Veritrans API.
     *
     * @param cardNumber        credit card number
     * @param cardCVV           credit card cvv number
     * @param cardExpiryMonth   credit card expiry month in number
     * @param cardExpiryYear    credit card expiry year in 4 digit (example: 2020)
     * @param clientKey         veritrans API client key
     *
     * @return observable of token
     */
    @Headers({"Content-Type: application/json", "x-auth: da53847171259b511488cf366e701050"})
    @GET("/card/register")
    Observable<CardRegistrationResponse> registerCard(
            @Query("card_number") String cardNumber,
            @Query("card_cvv") String cardCVV,
            @Query("card_exp_month") String cardExpiryMonth,
            @Query("card_exp_year") String cardExpiryYear,
            @Query("client_key") String clientKey
    );

    /**
     * Get authentication token.
     *
     * @return authentication token.
     */
    @POST("/auth")
    Observable<AuthModel> getAuthenticationToken();
}