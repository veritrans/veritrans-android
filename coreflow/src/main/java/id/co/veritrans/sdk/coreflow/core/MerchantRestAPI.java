package id.co.veritrans.sdk.coreflow.core;

import id.co.veritrans.sdk.coreflow.models.AuthModel;
import id.co.veritrans.sdk.coreflow.models.BBMMoneyRequestModel;
import id.co.veritrans.sdk.coreflow.models.BCABankTransfer;
import id.co.veritrans.sdk.coreflow.models.BCAKlikPayModel;
import id.co.veritrans.sdk.coreflow.models.CIMBClickPayModel;
import id.co.veritrans.sdk.coreflow.models.CardResponse;
import id.co.veritrans.sdk.coreflow.models.CardTransfer;
import id.co.veritrans.sdk.coreflow.models.DeleteCardResponse;
import id.co.veritrans.sdk.coreflow.models.EpayBriTransfer;
import id.co.veritrans.sdk.coreflow.models.GetOffersResponseModel;
import id.co.veritrans.sdk.coreflow.models.IndomaretRequestModel;
import id.co.veritrans.sdk.coreflow.models.IndosatDompetkuRequest;
import id.co.veritrans.sdk.coreflow.models.KlikBCAModel;
import id.co.veritrans.sdk.coreflow.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.coreflow.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.coreflow.models.MandiriECashModel;
import id.co.veritrans.sdk.coreflow.models.PermataBankTransfer;
import id.co.veritrans.sdk.coreflow.models.RegisterCardResponse;
import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;
import id.co.veritrans.sdk.coreflow.models.SaveCardResponse;
import id.co.veritrans.sdk.coreflow.models.TokenDetailsResponse;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by ziahaqi on 27/06/2016.
 */
public interface MerchantRestAPI {


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
     * @param klikBCAModel Klik BCA description
     * @return Observable of the Transaction Response object.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingKlikBCA(@Body KlikBCAModel klikBCAModel,
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


    //save cards or get cards from/to merchantAPI
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/card/register")
    void saveCard(@Header("x-auth") String auth,
                  @Body SaveCardRequest cardTokenRequest,
                  Callback<SaveCardResponse> callback);

    //save cards or get cards
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

    /**
     * Get authentication token from merchant server
     *
     * @return authentication token.
     */
    @POST("/auth")
    void getAuthenticationToken(Callback<AuthModel> callback);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/creditcard")
    void registerCard(@Header("x-auth") String auth,
                      @Body RegisterCardResponse registerCardResponse,
                      Callback<CardResponse> callback);


    /*
     * Snap End Point
     */

    @Headers({"Content-Type: application/json",
            "Accept: application/json"})
    @POST("/token/")
    void getSnapToken(Callback<TokenDetailsResponse> callback);


}
