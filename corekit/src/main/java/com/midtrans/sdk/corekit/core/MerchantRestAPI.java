package com.midtrans.sdk.corekit.core;

import com.midtrans.sdk.corekit.models.AuthModel;
import com.midtrans.sdk.corekit.models.BBMMoneyRequestModel;
import com.midtrans.sdk.corekit.models.BCABankTransfer;
import com.midtrans.sdk.corekit.models.BCAKlikPayModel;
import com.midtrans.sdk.corekit.models.CIMBClickPayModel;
import com.midtrans.sdk.corekit.models.CardResponse;
import com.midtrans.sdk.corekit.models.CardTransfer;
import com.midtrans.sdk.corekit.models.DeleteCardResponse;
import com.midtrans.sdk.corekit.models.EpayBriTransfer;
import com.midtrans.sdk.corekit.models.GetOffersResponseModel;
import com.midtrans.sdk.corekit.models.IndomaretRequestModel;
import com.midtrans.sdk.corekit.models.IndosatDompetkuRequest;
import com.midtrans.sdk.corekit.models.KlikBCAModel;
import com.midtrans.sdk.corekit.models.MandiriBillPayTransferModel;
import com.midtrans.sdk.corekit.models.MandiriClickPayRequestModel;
import com.midtrans.sdk.corekit.models.MandiriECashModel;
import com.midtrans.sdk.corekit.models.PermataBankTransfer;
import com.midtrans.sdk.corekit.models.RegisterCardResponse;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.TokenRequestModel;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.Token;

import java.util.ArrayList;

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

    /**
     * PAPI STUFF
     * Merchant end points that have been using  PAPI backend
     */

    /**
     * @param authorization       merchant token
     * @param permataBankTransfer Permata Bank request body
     * @param callback            Transaction response
     */
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
     * @param bcaBankTransfer bca bank tranfer request body
     * @return callback of transaction response
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingBCAVA(
            @Header("x-auth") String authorization,
            @Body BCABankTransfer bcaBankTransfer,
            Callback<TransactionResponse> callback);

    /**
     * Do the payment using credit card
     *
     * @param auth         token
     * @param cardTransfer card request body
     * @param callback     transaction response
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingCard(
            @Header("x-auth") String auth, @Body CardTransfer
            cardTransfer, Callback<TransactionResponse> callback);


    /**
     * Do the payment using mandiri click pay
     *
     * @param auth                        token
     * @param mandiriClickPayRequestModel mandiri click pay request body
     * @param callback                    transaction response
     */
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
     * @param callback        callback of the Transaction Response object.
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
     * @return callback response of transaction.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingKlikBCA(@Body KlikBCAModel klikBCAModel,
                             Callback<TransactionResponse> callback);


    /**
     * Do payment mandiri bill pay
     *
     * @param auth                        token
     * @param mandiriBillPayTransferModel Mandiri bill pay request body
     * @param callback                    transaction response
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingMandiriBillPay(@Header("x-auth") String auth,
                                    @Body MandiriBillPayTransferModel
                                            mandiriBillPayTransferModel,
                                    Callback<TransactionResponse> callback);

    /**
     * Do payment using bri epay
     *
     * @param auth            Token
     * @param epayBriTransfer epay bri request body
     * @param callback        transaction response
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingEpayBri(@Header("x-auth") String auth,
                             @Body EpayBriTransfer
                                     epayBriTransfer,
                             Callback<TransactionResponse> callback);

    /**
     * Do payment using Indosat Dompetku
     *
     * @param auth                   token
     * @param indosatDompetkuRequest indosat dompetku request body
     * @param callback               response transaction
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingIndosatDompetku(@Header("x-auth") String auth,
                                     @Body IndosatDompetkuRequest
                                             indosatDompetkuRequest,
                                     Callback<TransactionResponse> callback);

    /**
     * Do payment using CIMB click pay
     *
     * @param auth              token
     * @param cimbClickPayModel CIMB payment request body
     * @param callback          reponse transaction
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingCIMBClickPay(@Header("x-auth") String auth,
                                  @Body CIMBClickPayModel
                                          cimbClickPayModel,
                                  Callback<TransactionResponse> callback);

    /**
     * Do payment using Mandiri E Cash
     *
     * @param auth              token
     * @param mandiriECashModel Mandiri ecash request body
     * @param callback          Transaction response
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingMandiriECash(@Header("x-auth") String auth,
                                  @Body MandiriECashModel
                                          mandiriECashModel,
                                  Callback<TransactionResponse> callback);

    /**
     * Do payment using indomaret
     *
     * @param auth                  token
     * @param indomaretRequestModel Indomaret payment request body
     * @param callback              transaction response
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingIndomaret(@Header("x-auth") String auth,
                               @Body IndomaretRequestModel
                                       indomaretRequestModel,
                               Callback<TransactionResponse> callback);

    /**
     * Do payment using BBM Money
     *
     * @param auth                 token
     * @param bbmMoneyRequestModel BBM Money request body
     * @param callback             transaction response
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge/")
    void paymentUsingBBMMoney(@Header("x-auth") String auth,
                              @Body BBMMoneyRequestModel
                                      bbmMoneyRequestModel,
                              Callback<TransactionResponse> callback);

    /**
     * Save card to merchant server
     *
     * @param auth             token
     * @param cardTokenRequest Card token requst body
     * @param callback         response of save card request
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/card/register")
    void saveCard(@Header("x-auth") String auth,
                  @Body SaveCardRequest cardTokenRequest,
                  Callback<SaveCardResponse> callback);

    /**
     * Get saved cards from merchant server
     *
     * @param auth     token
     * @param callback response of get card reguest
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/card/")
    void getCard(@Header("x-auth") String auth, Callback<CardResponse> callback);

    /**
     * Remove save card from merchant server
     *
     * @param auth         token
     * @param savedTokenId save token id
     * @param callback     response of delete card request
     */
    @Deprecated
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @DELETE("/card/{saved_token_id}")
    void deleteCard(@Header("x-auth") String auth, @Path("saved_token_id") String savedTokenId,
                    Callback<DeleteCardResponse> callback);

    /**
     * Get offers from merchant backend
     *
     * @param auth     token
     * @param callback response of Get Offers request
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/promotions")
    void getOffers(@Header("x-auth") String auth, Callback<GetOffersResponseModel> callback);

    /**
     * Get authentication token from merchant server
     *
     * @param callback authentication token.
     */
    @POST("/auth")
    void getAuthenticationToken(Callback<AuthModel> callback);

    /**
     * Register card to PAPI backend
     *
     * @param auth                 token
     * @param registerCardResponse Register card request body
     * @param callback             response of card registration request
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/creditcard")
    void registerCard(@Header("x-auth") String auth,
                      @Body RegisterCardResponse registerCardResponse,
                      Callback<CardResponse> callback);

    /*
     * SNAP TOKEN STUFF
     * Merchant end point that have been using Snap Backend
     */

    /**
     * Get snap token.
     *
     * @param requestModel SnapToken RequestModel
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/charge")
    void checkout(@Body TokenRequestModel requestModel, Callback<Token> callback);

    /**
     * save cards to merchant server
     *
     * @param userId            unique id for every user
     * @param saveCardsRequests list of saved credit cards
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/users/{user_id}/tokens")
    void saveCards(@Path("user_id") String userId, @Body ArrayList<SaveCardRequest> saveCardsRequests,
                   Callback<String> callback);

    /**
     * get cards from merchant server
     *
     * @param userId unique id for every user
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/users/{user_id}/tokens")
    void getCards(@Path("user_id") String userId, Callback<ArrayList<SaveCardRequest>> callback);
}
